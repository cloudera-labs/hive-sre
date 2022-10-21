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

//import spec.bridge.hadoop2.KafkaOutputFormat;

import org.apache.commons.cli.*;
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

//import static com.streever.iot.data.mapreduce.DataGenMapper.SCHEMA_FILE;

public class MRPingTool extends Configured implements Tool {

    static private Logger LOG = Logger.getLogger(MRPingTool.class.getName());
    public static final String HOST_LIST_FILE = "host.file";
    public static final String BATCH_ID = "batch.id";

    private Options options;
    private Path outputPath;
    public static final int DEFAULT_MAPPERS = 2;
    public static final long DEFAULT_COUNT = 5;

    public MRPingTool() {
        buildOptions();
    }

    private void buildOptions() {
        options = new Options();

        Option help = new Option("h", "help", false, "Help");
        help.setRequired(false);

        Option outputDir = new Option("d", "directory", true, "Output Directory [REQUIRED]");
        outputDir.setRequired(Boolean.TRUE);

        Option mappers = new Option("m", "mappers", true, "Number of Mappers.  To get coverage, should be more than the compute node count.  But no guarantee of even distribution, so best to over subscribe. Default 2");
        mappers.setRequired(false);

        Option hlFile = new Option("hl", "host-list", true, "The host list file on HDFS. A text file with a FQHN (full qualified host name) per line.[REQUIRED]");
        hlFile.setRequired(Boolean.TRUE);
        options.addOption(hlFile);

        Option count = new Option("c", "count", true, "Ping Count (The number of iterations we'll run ping).  Each ping request makes 5 pings.\n" +
                "                         So if this value is 3, we'll do 3 sets of 5 pings (15 total). Default 5");
        count.setRequired(false);

        options.addOption(help);
        options.addOption(mappers);
        options.addOption(outputDir);
        options.addOption(count);
    }

    private void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("hadoop jar <jar-file> " + this.getClass().getCanonicalName(), options);
    }

    protected void setup(Job job) {
        // Get the conf location from the job conf.

        String config = job.getConfiguration().get(HOST_LIST_FILE);
            LOG.info("Host List File: " + config);

        job.setNumReduceTasks(0);
        job.setMapperClass(MRCommandMapper.class);
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

        job.setInputFormatClass(MRCommandInputFormat.class);

        if (line.hasOption("c")) {
            MRCommandInputFormat.setNumberOfRows(job, Long.parseLong(line.getOptionValue("c")));
        } else {
            MRCommandInputFormat.setNumberOfRows(job, DEFAULT_COUNT);
        }

        if (line.hasOption("m")) {
            int parallelism = Integer.parseInt(line.getOptionValue("m"));
            configuration.setInt(MRJobConfig.NUM_MAPS, Integer.parseInt(line.getOptionValue("m")));
        } else {
            // Default
            configuration.setInt(MRJobConfig.NUM_MAPS, DEFAULT_MAPPERS);
        }

        job.setInputFormatClass(MRCommandInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapOutputValueClass(Text.class);

        outputPath = new Path(line.getOptionValue("d", "MRPing-default"));
        FileOutputFormat.setOutputPath(job, outputPath);

        // One of these is set.
        if (line.hasOption("hl")) {
            job.getConfiguration().set(HOST_LIST_FILE, line.getOptionValue("hl"));
        }

        // Create and set a batch ID
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String batchId = df.format(new Date());
        job.getConfiguration().set(BATCH_ID, batchId);
        System.out.println("Batch ID: " + batchId);

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

        job.setJarByClass(MRPingTool.class);

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
        result = ToolRunner.run(new Configuration(), new MRPingTool(), args);
        System.exit(result);
    }
}
