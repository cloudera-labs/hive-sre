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

package com.cloudera.utils;

import com.cloudera.framework.sqlreports.DBAAnalysisFramework;
import com.cloudera.utils.hive.HiveFrameworkCheck;
import com.cloudera.utils.hive.perf.JDBCPerfTest;
import com.cloudera.utils.stats.StatsFramework;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Arrays;

public class Sre {
    private static final Logger LOG = LogManager.getLogger(Sre.class);

    public static void main(String[] args) {
        Sre sre = new Sre();
        try {
            sre.init(args);
//            System.exit(0);
        } catch (Throwable t) {
            System.err.println(t.getMessage());
            t.printStackTrace();
//            System.exit(0);
        }
    }

    public void init(String[] args) throws Throwable {
        // Check first item in args.  It should be one of:
        // perf
        // sre
        // u3
        // cust
        // tc - transaction cleanup
        String subApp = null;
        if (args.length > 0 && args[0].matches("perf|sre|u3|u3e|cust|dba|stats")) {
            System.err.println("Launching: " + args[0]);
            subApp = args[0];
        } else {
            System.out.println("First element must be one of: perf,sre,u3,cust,dba,stats");
            System.exit(-1);
        }

        // Remove the first item from the args and pass on to sub application
        String[] appArgs = Arrays.copyOfRange(args, 1, args.length);

        // Then choose which sub application to start
        SreSubApp sreApp = null;
        switch (subApp) {
            case "perf":
                sreApp = new JDBCPerfTest("perf");
                break;
            case "sre":
                sreApp = new HiveFrameworkCheck("sre", "/sre/proc/hive_sre_procs.yaml");
                break;
            case "u3":
                sreApp = new HiveFrameworkCheck("u3", "/u3/proc/hive_u3_procs.yaml");
                break;
            case "u3e":
                sreApp = new HiveFrameworkCheck("u3e", "/u3e/proc/hive_u3e_procs.yaml");
                break;
            case "cust":
                sreApp = new HiveFrameworkCheck();
                break;
            case "tc":
                sreApp = new HiveFrameworkCheck("tc", "/tc/procs/hive_tc_procs.yaml");
                break;
            case "dba":
                sreApp = new DBAAnalysisFramework();
                break;
            case "stats":
                sreApp = new StatsFramework();
                break;
        }

        if (sreApp.init(appArgs)) {
            LOG.info("sre-app: " + sreApp.getName() + " successfully initialized");
            sreApp.start();
        } else {
            LOG.info("sre-app: " + sreApp.getName() + " reporting");
            sreApp.report();
        }

    }

}
