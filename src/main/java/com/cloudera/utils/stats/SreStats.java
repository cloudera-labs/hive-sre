package com.cloudera.utils.stats;

import com.cloudera.utils.hadoop.HadoopSession;
import com.cloudera.utils.hadoop.util.RecordConverter;
import com.cloudera.utils.hadoop.yarn.Stats;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public abstract class SreStats extends TimerTask {

    private HadoopSession cli = null;
    private Options options = null;
    private String[] initArgs = null;

    private Stats stats;

    protected HadoopSession getCli() {
        return cli;
    }

    protected Stats getStats() {
        return stats;
    }

    protected void setStats(Stats stats) {
        this.stats = stats;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public String[] getInitArgs() {
        return initArgs;
    }

    public void setInitArgs(String[] initArgs) {
        this.initArgs = initArgs;
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            Options condensedOptions = new Options();
            // Need to make sure the options are set first...
            for (Object cOpt : getOptions().getOptions()) {
                if (cOpt instanceof Option) {
                    condensedOptions.addOption((Option) cOpt);
                } else if (cOpt instanceof OptionGroup) {
                    condensedOptions.addOptionGroup((OptionGroup) cOpt);
                }
            }
            for (Object cOpt : stats.getOptions().getOptions()) {
                if (cOpt instanceof Option) {
                    condensedOptions.addOption((Option) cOpt);
                } else if (cOpt instanceof OptionGroup) {
                    condensedOptions.addOptionGroup((OptionGroup) cOpt);
                }
            }

            cmd = parser.parse(condensedOptions, initArgs);
            stats.init(cmd);

        } catch (ParseException pe) {
            HelpFormatter formatter = new HelpFormatter();
//            String cmdline = ReportingConf.substituteVariablesFromManifest(USAGE_CMD + "\nversion:${Implementation-Version}");
//            formatter.printHelp(100, cmdline, "Hive SRE Utility", options,
//                    "\nVisit https://github.com/cloudera-labs/hive-sre for detailed docs");
            System.err.println(pe.getMessage());
            System.exit(-1);
        }

    }

    public SreStats() {
        String[] api = {"-api"};
        cli = new HadoopSession();
        try {
            if (!cli.start(api)) {
                throw new RuntimeException("Issue connecting to DFS.  Check Kerberos Auth and configs");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void print(String[] fields, List<Map<String, Object>> records) {
        if (records != null) {
            int i = 0;
            try {
                StringBuilder sb = new StringBuilder();
//            if (header)
                sb.append(StringUtils.join(fields, " | ")).append("\n");
                for (Map<String, Object> record : records) {
                    i++;
                    if (i % 8000 == 0)
                        System.out.println(".");
                    else if (i % 100 == 0)
                        System.out.print(".");

                    String recordStr = RecordConverter.mapToRecord(fields, record, " | ");

                    sb.append(recordStr).append("\n");
                }
                System.out.println(sb.toString());
            } catch (Throwable t) {
                t.printStackTrace();
                throw t;
            }
        }
    }

}
