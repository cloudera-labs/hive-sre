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

package com.cloudera.utils.hive.perf;

import com.cloudera.utils.SreSubApp;
import org.apache.commons.cli.*;

import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class JDBCPerfTest implements SreSubApp {

    public static final Integer STATUS_INTERVAL_SECS = 1;
    public static final Integer REFRESH_INTERVAL = 1000;
    public static final Integer DISPLAY_REFRESH_SECS = 1;

    private JDBCRecordIterator jri = new JDBCRecordIterator();
    private CollectStatistics stats = null;

    private String name;

    private ScheduledExecutorService threadPool;
    private List<ScheduledFuture> processThreads;

    public JDBCPerfTest(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScheduledExecutorService getThreadPool() {
        if (threadPool == null) {
            threadPool = Executors.newScheduledThreadPool(2);
        }
        return threadPool;
    }

    public List<ScheduledFuture> getProcessThreads() {
        if (processThreads == null) {
            processThreads = new ArrayList<ScheduledFuture>();
        }
        return processThreads;
    }

    public JDBCRecordIterator getJri() {
        return jri;
    }

    public CollectStatistics getStats() {
        return stats;
    }

    public Options getOptions() {
        Options options = new Options();

        Option url = new Option("u", "url", true,
                "Database URL");
        url.setRequired(true);
        options.addOption(url);

        Option username = new Option("n", "username", true, "Username");
        username.setRequired(false);
        options.addOption(username);

        Option password = new Option("p", "password", true, "Password");
        password.setRequired(false);
        options.addOption(password);

        Option query = new Option("e", "query", true, "Query");
        query.setRequired(true);
        options.addOption(query);

        Option lite = new Option("l", "lite", false, "Don't open record.  Reduce client overhead (loose some stats)");
        lite.setRequired(false);
        options.addOption(lite);

        Option commment = new Option("c", "comment", true, "Comment");
        commment.setRequired(false);
        options.addOption(commment);

        Option batchsize = new Option("b", "batch-size", true, "Client Batch Fetch Size");
        batchsize.setArgs(1);
        batchsize.setRequired(false);
        options.addOption(batchsize);

        Option hostCheck = new Option("h", "host-check", true, "Ping a target host");
        hostCheck.setArgs(1);
        hostCheck.setRequired(false);
        options.addOption(hostCheck);

        Option delayWarning = new Option("d", "delay-warning", true,
                "Warn when delay while iterating over records is greater than this.");
        delayWarning.setArgs(1);
        delayWarning.setRequired(false);
        options.addOption(delayWarning);

        // TBD WIP
//        Option execFile = Option.builder("f").required(false)
//                .argName("exec file").desc("Execute File")
//                .longOpt("exec")
//                .hasArg(true).numberOfArgs(1)
//                .build();
//        options.addOption(execFile);
//
//        Option batchSize = Option.builder("b").required(false)
//                .argName("batch").desc("Batch Size")
//                .longOpt("batch")
//                .hasArg(true).numberOfArgs(1)
//                .build();
//        options.addOption(batchSize);

        return options;
    }


    private void setOptions(Options options, String[] args) {

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(this.getClass().getName(), options);
            System.exit(-1);
        }

        if (cmd.hasOption("u")) {
            getJri().setJdbcUrl(cmd.getOptionValue("u"));
        }

        if (cmd.hasOption("n")) {
            getJri().setUsername(cmd.getOptionValue("n"));
        } else {
            getJri().setUsername(System.getProperty("user.name"));
        }

        if (cmd.hasOption("p")) {
            getJri().setPassword(cmd.getOptionValue("p"));
        }

        if (cmd.hasOption("e")) {
            getJri().setQuery(cmd.getOptionValue("e"));
        }

        if (cmd.hasOption("l")) {
            getJri().setLite(Boolean.TRUE);
        }

        if (cmd.hasOption("b")) {
            String value = cmd.getOptionValue("b");
            getJri().setBatchSize(Integer.valueOf(value));
        }

        if (cmd.hasOption("h")) {
            String value = cmd.getOptionValue("h");
            getJri().setPingHost(value);
        }

        if (cmd.hasOption("d")) {
            String value = cmd.getOptionValue("d");
            getJri().setDelayWarning(Integer.valueOf(value));
        }

        stats = new CollectStatistics(getJri());
        if (cmd.hasOption("c")) {
            stats.setComment(cmd.getOptionValue("c"));
        }
    }

    public Boolean init(String[] args) {
        Options options = getOptions();
        setOptions(options, args);
        return Boolean.TRUE;
    }

    @Override
    public void report() {
        //
    }

    public void start() {
        getProcessThreads().add(getThreadPool().schedule(this.getJri(), 1, MILLISECONDS));
        getProcessThreads().add(getThreadPool().schedule(this.getStats(), 1000, MILLISECONDS));
//        getStats().start();

        while (true) {
            boolean check = true;
            for (ScheduledFuture sf : getProcessThreads()) {
                if (!sf.isDone()) {
                    check = false;
                    break;
                }
            }
            if (check)
                break;
            try {
                TimeUnit.SECONDS.sleep(DISPLAY_REFRESH_SECS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getStats().printStatus(Boolean.FALSE);
        }
        getStats().printStatus(Boolean.TRUE);
        getThreadPool().shutdown();
//        getStats().interrupt();

    }

    public static void main(String[] args) {
        JDBCPerfTest test = new JDBCPerfTest("perf");
        test.init(args);
        test.start();
    }

}