/*
 * Copyright 2021 Cloudera, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.utils.hive;

import com.cloudera.utils.hive.perf.JDBCPerfTest;

import java.util.Arrays;

public class Sre {

    public static void main(String[] args) {
        Sre sre = new Sre();
        try {
            sre.init(args);
            System.exit(0);
        } catch (Throwable t) {
            System.err.println(t.getMessage());
            t.printStackTrace();
            System.exit(0);
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
        if (args.length > 0 && args[0].matches("perf|sre|u3|cust")) {
            System.err.println("Launching: " + args[0]);
            subApp = args[0];
        } else {
            System.out.println("First element must be one of: perf,sre,u3,cust");
            System.exit(-1);
        }

        // Remove the first item from the args and pass on to sub application
        String[] appArgs = Arrays.copyOfRange(args, 1, args.length);

        // Then choose which sub application to start
        SreSubApp sreApp = null;
        switch (subApp) {
            case "perf":
                sreApp = new JDBCPerfTest();
                break;
            case "sre":
                sreApp = new HiveFrameworkCheck("sre", "/procs/hive_sre_procs.yaml");
                break;
            case "u3":
                sreApp = new HiveFrameworkCheck("u3", "/procs/h3_upg_procs.yaml");
                break;
            case "cust":
                sreApp = new HiveFrameworkCheck();
                break;
            case "tc":
                sreApp = new HiveFrameworkCheck("tc", "/procs/transaction_procs.yaml");
                break;
        }

        sreApp.init(appArgs);
        sreApp.start();

    }

}
