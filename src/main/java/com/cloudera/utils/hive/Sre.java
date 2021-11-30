package com.cloudera.utils.hive;

import com.cloudera.utils.hive.perf.JDBCPerfTest;

import java.util.Arrays;

/**
 * Hello world!
 */
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
                sreApp = new HiveFrameworkCheck("/procs/hive_sre_procs.yaml");
                break;
            case "u3":
                sreApp = new HiveFrameworkCheck("/procs/h3_upg_procs.yaml");
                break;
            case "cust":
                sreApp = new HiveFrameworkCheck();
                break;
            case "tc":
                sreApp = new HiveFrameworkCheck("/procs/transaction_procs.yaml");
                break;
        }

        sreApp.init(appArgs);
        sreApp.start();

    }

}
