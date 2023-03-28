package com.cloudera.utils.stats;

import com.cloudera.utils.Protect;
import com.cloudera.utils.SreSubApp;
import com.cloudera.utils.hadoop.HadoopSession;
import com.cloudera.utils.hadoop.HadoopSessionFactory;
import com.cloudera.utils.hadoop.HadoopSessionPool;
import com.cloudera.utils.hadoop.yarn.ContainerStats;
import com.cloudera.utils.hadoop.yarn.SchedulerStats;
import com.cloudera.utils.hive.config.SreProcessesConfig;
import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.hive.sre.MessageCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Timer;

public class StatsFramework implements SreSubApp {
    private static final Logger LOG = LogManager.getLogger(StatsFramework.class);
    private final String USAGE_CMD = "hive-sre stats ";
    private SreProcessesConfig config;
    private SreStats stats = null;

    private Boolean collectionMode = Boolean.FALSE;

    public SreProcessesConfig getConfig() {
        return config;
    }

    public void setConfig(SreProcessesConfig config) {
        this.config = config;
    }

    public Boolean isCollectionMode() {
        return collectionMode;
    }

    public void setCollectionMode(Boolean collectionMode) {
        this.collectionMode = collectionMode;
    }

    @Override
    public Boolean init(String[] args) {
        Boolean rtn = Boolean.TRUE;
        Options options = getOptions();

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            HelpFormatter formatter = new HelpFormatter();
            String cmdline = ReportingConf.substituteVariablesFromManifest(USAGE_CMD + "\nversion:${Implementation-Version}");
            formatter.printHelp(100, cmdline, "Hive SRE Utility", options,
                    "\nVisit https://github.com/cloudera-labs/hive-sre for detailed docs");
            System.err.println(pe.getMessage());
            System.exit(-1);
        }

        if (cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
//            System.out.println(ReportingConf.substituteVariables("v.${Implementation-Version}"));
            String cmdline = ReportingConf.substituteVariablesFromManifest(USAGE_CMD + "\nversion:${Implementation-Version}");
            formatter.printHelp(100, cmdline, "Hive SRE Utility", options,
                    "\nVisit https://github.com/cloudera-labs/hive-sre for detailed docs");

//            formatter.printHelp("Sre", options);
            System.exit(-1);
        }

        // Initialize with config and output directory.
        String configFile = null;
        if (cmd.hasOption("cfg")) {
            configFile = cmd.getOptionValue("cfg");
            System.out.println("You've specified a custom config file. Try editing/using the 'default.yaml' in the $HOME/.hive-sre/cfg directory instead.");
        } else {
            configFile = System.getProperty("user.home") + System.getProperty("file.separator") + ".hive-sre/cfg/default.yaml";
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            File cfgFile = new File(configFile);
            if (!cfgFile.exists()) {
                throw new RuntimeException("Missing configuration file: " + configFile);
            } else {
                System.out.println("Using Config: " + configFile);
            }
            String yamlCfgFile = FileUtils.readFileToString(cfgFile, Charset.forName("UTF-8"));
            SreProcessesConfig sreConfig = mapper.readerFor(SreProcessesConfig.class).readValue(yamlCfgFile);
            sreConfig.validate();
            setConfig(sreConfig);
        } catch (
                IOException e) {
            throw new RuntimeException("Issue getting configs", e);
        }

        if (cmd.hasOption("p") || cmd.hasOption("dp")) {
            // Used to generate encrypted password.
            rtn = Boolean.FALSE;
            if (cmd.hasOption("pkey")) {
                Protect protect = new Protect(cmd.getOptionValue("pkey"));
                // Set to control execution flow.
                if (cmd.hasOption("p")) {
                    String epassword = null;
                    try {
                        epassword = protect.encrypt(cmd.getOptionValue("p"));
                        config.getWarnings().set(MessageCode.ENCRYPT_PASSWORD.getCode(), epassword);
                    } catch (Exception e) {
                        config.getErrors().set(MessageCode.ENCRYPT_PASSWORD_ISSUE.getCode());
                    }
                } else {
                    String password = null;
                    try {
                        password = protect.decrypt(cmd.getOptionValue("dp"));
                        config.getWarnings().set(MessageCode.DECRYPT_PASSWORD.getCode(), password);
                    } catch (Exception e) {
                        config.getErrors().set(MessageCode.DECRYPTING_PASSWORD_ISSUE.getCode());
                    }
                }
            } else {
                config.getErrors().set(MessageCode.PKEY_PASSWORD_CFG.getCode());
            }
        } else {
            // When the pkey is specified, we assume the config passwords are encrytped and we'll decrypt them before continuing.
            if (cmd.hasOption("pkey")) {
                // Loop through the HiveServer2 Configs and decode the password.
                System.out.println("Password Key specified.  Decrypting config password before submitting.");

                String pkey = cmd.getOptionValue("pkey");
                Protect protect = new Protect(pkey);

                if (config.getQueryAnalysis() != null) {
                    Properties props = config.getQueryAnalysis().getConnectionProperties();
                    String password = props.getProperty("password");
                    if (password != null) {
                        try {
                            String decryptedPassword = protect.decrypt(password);
                            props.put("password", decryptedPassword);
                        } catch (Exception e) {
                            config.getErrors().set(MessageCode.DECRYPTING_PASSWORD_ISSUE.getCode());
                        }
                    }
                }
            }
        }

        if (cmd.hasOption("cs")) {
            stats = new SreContainerStats();
        } else if (cmd.hasOption("ss")) {
            stats = new SreSchedulerStats();
        }
        stats.setOptions(getOptions());
        stats.setInitArgs(args);

        // Setup Collection Mode
        if (cmd.hasOption("cm")) {
            System.out.println("Collection Mode");
            setCollectionMode(Boolean.TRUE);
        }

        return rtn;
    }

    @Override
    public void start() {
        Timer timer = new Timer("Timer");
        long delay = 100l;
        if (isCollectionMode()) {
            if (stats instanceof SreSchedulerStats) {
                // Schedule every minute
                long minInterval = 60 * 1000l;
                timer.scheduleAtFixedRate(stats, delay, minInterval);
            } else if (stats instanceof SreContainerStats) {
                // Schedule every hour..
                // TODO: Need to adjust run parameters for stats collection to ensure we're only pull last hour
            }
        } else {
            timer.schedule(stats, delay);
        }
    }

    @Override
    public void report() {

    }

    @Override
    public String getName() {
        return "Stats";
    }

    private Options getOptions() {
        // create Options object
        Options options = new Options();

        Option cfgOption = new Option("cfg", "config", true,
                "Config with details for the Sre Job.  Must match the either sre or u3 selection. Default: $HOME/.hive-sre/cfg/default.yaml");
        cfgOption.setRequired(false);
        options.addOption(cfgOption);

        OptionGroup procOptions = new OptionGroup();
        Option helpOption = new Option("h", "help", false,
                "Help");
        helpOption.setRequired(false);
        procOptions.addOption(helpOption);

//        Option sslOption = new Option("ssl", "ssl", false,
//                "Use 'ssl' when connecting to YARN");
//        sslOption.setRequired(false);
//        options.addOption(sslOption);

        Option csOption = new Option("cs", "container-stats", false,
                "Container Stats");
        csOption.setRequired(false);
        procOptions.addOption(csOption);

        Option ssOption = new Option("ss", "scheduler-stats", false,
                "Scheduler Stats");
        ssOption.setRequired(false);
        procOptions.addOption(ssOption);

        Option pwOption = new Option("p", "password", true,
                "Used this in conjunction with '-pkey' to generate the encrypted password that you'll add to the configs for the JDBC connections.");
        pwOption.setRequired(Boolean.FALSE);
        pwOption.setArgName("password");
        procOptions.addOption(pwOption);

        Option decryptPWOption = new Option("dp", "decrypt-password", true,
                "Used this in conjunction with '-pkey' to decrypt the generated passcode from `-p`.");
        decryptPWOption.setRequired(Boolean.FALSE);
        decryptPWOption.setArgName("encrypted-password");
        procOptions.addOption(decryptPWOption);

        procOptions.setRequired(Boolean.FALSE);
        options.addOptionGroup(procOptions);

//        Option cronOption = new Option("cron", "cron", true,
//                "CRON setting to control run schedule. When specified, application will run the desired actions " +
//                        "on the cron schedule.  Best to run the application in the background/detached");
//        cronOption.setRequired(Boolean.FALSE);
//        options.addOption(cronOption);

        Option collectionModeOption = new Option("cm", "collection-mode", false,
                "Run stats in collection-mode.  Mean the process will run continually and collect details for " +
                        "the targeted stats.  `-cs` will run every hour and `-ss` will run every minute.");
        collectionModeOption.setRequired(Boolean.FALSE);
        options.addOption(collectionModeOption);

        Option pKeyOption = new Option("pkey", "password-key", true,
                "The key used to encrypt / decrypt the cluster jdbc passwords.  If not present, the passwords will be processed as is (clear text) from the config file.");
        pKeyOption.setRequired(false);
        pKeyOption.setArgName("password-key");
        options.addOption(pKeyOption);

        ContainerStats cStats = new ContainerStats();
        for (Object cOpt: cStats.getOptions().getOptions()) {
            if (cOpt instanceof Option) {
                options.addOption((Option)cOpt);
            } else if (cOpt instanceof OptionGroup) {
                options.addOptionGroup((OptionGroup)cOpt);
            }
        }

        SchedulerStats sStats = new SchedulerStats();
        for (Object cOpt: sStats.getOptions().getOptions()) {
            if (cOpt instanceof Option) {
                options.addOption((Option)cOpt);
            } else if (cOpt instanceof OptionGroup) {
                options.addOptionGroup((OptionGroup)cOpt);
            }
        }

        return options;
    }

}
