/*
 * Copyright (c) 2022. Cloudera, Inc. All Rights Reserved
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.cloudera.utils.mapreduce;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MRBusyTool extends Configured implements Tool {

    static private Logger LOG = Logger.getLogger(MRBusyTool.class.getName());
    public static final String PAUSE_INCREMENT = "busy.pause.increment";
    public static final int PAUSE_INCREMENT_DEFAULT = 1;
    private char[] chars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private Options options;
    private Path outputPath;
    public static final int DEFAULT_MAPPERS = 2;
    public static final long DEFAULT_COUNT = 5;

    public MRBusyTool() {
        buildOptions();
    }

    private void buildOptions() {
        options = new Options();

        Option help = new Option("h", "help", false, "Help");
        help.setRequired(false);

        Option mappers = new Option("m", "mappers", true, "Number of Mappers.  To get coverage, should be more than the compute node count.  But no guarantee of even distribution, so best to over subscribe. Default 2");
        mappers.setRequired(false);

        Option busyWaitTime = new Option("w", "wait-pause", true, "The time to 'pause' (secs) in between record writes. This should not be too long (a few seconds max) to avoid Mapper Timeouts.  Increase count to get longer job runs.");
        busyWaitTime.setRequired(Boolean.FALSE);

        Option count = new Option("c", "count", true, "Record Count");

        count.setRequired(false);

        options.addOption(help);
        options.addOption(mappers);
        options.addOption(busyWaitTime);
        options.addOption(count);
    }

    private void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("hadoop jar <jar-file> " + this.getClass().getCanonicalName(), options);
    }

    protected void setup(Job job) {
        // Get the conf location from the job conf.

        job.setNumReduceTasks(0);
        job.setMapperClass(MRBusyMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setNumReduceTasks(0);

    }

    private boolean checkUsage(String[] args, Job job) {
        boolean rtn = true;
        Configuration configuration = job.getConfiguration();

        CommandLineParser clParser = new PosixParser();

        CommandLine line = null;
        try {
            line = clParser.parse(options, args);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return false;
        }

        if (line.hasOption("h")) {
            return false;
        }

        job.setInputFormatClass(MRStaticInputFormat.class);

        if (line.hasOption("c")) {
            MRStaticInputFormat.setNumberOfRows(job, Long.parseLong(line.getOptionValue("c")));
        } else {
            MRStaticInputFormat.setNumberOfRows(job, DEFAULT_COUNT);
        }

        if (line.hasOption("w")) {
            configuration.setInt(PAUSE_INCREMENT, Integer.parseInt(line.getOptionValue("w")));
        }

        if (line.hasOption("m")) {
            configuration.setInt(MRJobConfig.NUM_MAPS, Integer.parseInt(line.getOptionValue("m")));
        } else {
            // Default
            configuration.setInt(MRJobConfig.NUM_MAPS, DEFAULT_MAPPERS);
        }

        configuration.setInt(MRJobConfig.MAP_MEMORY_MB, 1024); // 128 Mb
//        configuration.setInt(MRJobConfig.)
        job.setInputFormatClass(MRStaticInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapOutputValueClass(Text.class);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
        String outputPathStr = "busy_mr_job" + "/" + df.format(new Date()) + "_" + RandomStringUtils.random(10, chars);
        outputPath = new Path(outputPathStr);
        FileOutputFormat.setOutputPath(job, outputPath);

        return rtn;
    }


    public int run(String[] args) throws Exception {

        Job job = Job.getInstance(getConf()); // new Job(conf, this.getClass().getCanonicalName());

        System.out.println("Checking Input");
        if (!checkUsage(args, job)) {
            printUsage();
            return -1;
        }

        setup(job);

        job.setJarByClass(MRBusyTool.class);

        if (outputPath == null || outputPath.getFileSystem(job.getConfiguration()).exists(outputPath)) {
            throw new IOException("Output directory " + outputPath +
                    " already exists OR is missing from parameters list.");
        }

        int rtn_code = 0;

        try {
            rtn_code = job.waitForCompletion(true) ? 0 : 1;
        } catch (RuntimeException rte) {
            rte.fillInStackTrace();
            rte.printStackTrace();
        }
        return rtn_code;

    }


    public static void main(String[] args) throws Exception {
        int result;
        result = ToolRunner.run(new Configuration(), new MRBusyTool(), args);
        System.exit(result);
    }
}
