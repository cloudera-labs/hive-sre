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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.cloudera.utils.hive.config.HiveStrictManagedMigrationIncludeListConfig;
import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.hive.sre.DbSetProcess;
import com.cloudera.utils.hive.sre.ProcessContainer;
import com.cloudera.utils.hive.sre.SreProcessBase;
import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
public class HiveFrameworkCheck implements SreSubApp {
    private String name;
    private String stackResource;
    private ProcessContainer processContainer;
//    private String[] dbsOverride;
    private String outputDirectory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String[] getDbsOverride() {
//        return dbsOverride;
//    }
//
//    public void setDbsOverride(String[] dbsOverride) {
//        this.dbsOverride = dbsOverride;
//    }
//
    public ProcessContainer getProcessContainer() {
        return processContainer;
    }

    public void setProcessContainer(ProcessContainer processContainer) {
        this.processContainer = processContainer;
    }

    public String getStackResource() {
        return stackResource;
    }

    public void setStackResource(String stackResource) {
        this.stackResource = stackResource;
    }

    public HiveFrameworkCheck(String name, String stackResource) {
        this.name = name;
        this.stackResource = stackResource;
    }

    public HiveFrameworkCheck() {
    }

    //    public static void main(String[] args) {
//        HiveFrameWorkCheck sre = new HiveFrameWorkCheck();
//        try {
//            sre.init(args);
//            sre.start();
//            System.exit(0);
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//
//    }

    public void start() {
        getProcessContainer().run();
        // Check the Hsmm object for content.  Save to file.
        HiveStrictManagedMigrationIncludeListConfig hsmm = HiveStrictManagedMigrationIncludeListConfig.getInstance();
        ObjectMapper mapper;
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String hsmmStr = null;
        File hsmmFile = null;
        FileWriter hsmmFileWriter = null;
        try {
            hsmmStr = mapper.writeValueAsString(hsmm);
            hsmmFile = new File(outputDirectory + System.getProperty("file.separator") + "hsmm_includelist.yaml");
            hsmmFileWriter = new FileWriter(hsmmFile);
            hsmmFileWriter.write(hsmmStr);
            System.out.println("HSMM IncludeList File 'saved' to: " + hsmmFile.getPath());
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            System.err.println("Problem 'reading' HSMM IncludeList object");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Problem 'writing' HSMM IncludeList File");
        } finally {
            try {
                hsmmFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init(String[] args) {

        Options options = getOptions();

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Sre", options);
            System.err.println(pe.getMessage());
            System.exit(-1);
        }

        if (cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            System.out.println(ReportingConf.substituteVariables("v.${Implementation-Version}"));
            formatter.printHelp("Sre", options);
            System.exit(-1);
        }

        if (getStackResource() == null) {
            if (cmd.hasOption("hfw")) {
                setStackResource(cmd.getOptionValue("hfw"));
            }
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

//        if (cmd.hasOption("db")) {
//            dbsOverride = cmd.getOptionValues("db");
//        }

        // Should be set by now. If 'cust' option used, then it's set above with the -hfw option.  If that wasn't
        // present, this gets triggered.
        if (getStackResource() == null) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("'cust' requires `-hfw` <framework_resource>", options);
            System.exit(-1);
        }

        ProcessContainer procContainer = null;
        // First look for it as a Resource (in classpath)
        URL configURL = this.getClass().getResource(getStackResource());
        if (configURL != null) {
            String yamlConfigDefinition = null;
            try {
                yamlConfigDefinition = IOUtils.toString(configURL);
            } catch (IOException e) {
                throw new RuntimeException("Issue converting config: " + getStackResource(), e);
            }
            try {
                procContainer = mapper.readerFor(ProcessContainer.class).readValue(yamlConfigDefinition);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Issue deserializing config: " + getStackResource(), e);
            }
        } else {
            throw new RuntimeException("Couldn't locate 'HiveFramework Configuration File': " +
                    configURL.toString());
        }

        if (cmd.hasOption("i")) {
            String[] includeIds = cmd.getOptionValues("i");
            List<String> includes = Arrays.asList(includeIds);
            // Disable all procs
            for (SreProcessBase proc : procContainer.getProcesses()) {
                proc.setSkip(true);
                proc.setActive(false);
            }
            // Enable procs in 'include'
            for (SreProcessBase proc : procContainer.getProcesses()) {
                if (includes.contains(proc.getId())) {
                    proc.setActive(true);
                    proc.setSkip(false);
                }
            }
        }
        setProcessContainer(procContainer);

        // If specified, skip command checks.
        if (cmd.hasOption("scc") && !cmd.hasOption("cdh")) {
            for (SreProcessBase processBase: procContainer.getProcesses()) {
                if (processBase instanceof DbSetProcess) {
                    ((DbSetProcess)processBase).setCommandChecks(null);
                }
            }
        }

        /*
        THE FOLLOWING SHOULD BE MOVED TO CONFIGS IF MORE CONFIGS ARE CREATED.
        THIS IS A QUICK FIX FOR USABILITY in 'u3'. ;)
         */
        if (cmd.hasOption("cdh") && this.getName().equalsIgnoreCase("u3")) {
            String[] includeIds = {"1","3","5"};
            List<String> includes = Arrays.asList(includeIds);
            for (SreProcessBase proc : procContainer.getProcesses()) {
                if (includes.contains(proc.getId())) {
                    proc.setActive(Boolean.TRUE);
                    proc.setSkip(Boolean.FALSE);
                    if (proc.getId().equals("3")) {
                        ((DbSetProcess)proc).setCommandChecks(null);
                    }
                } else {
                    proc.setActive(Boolean.FALSE);
                    proc.setSkip(Boolean.TRUE);
                }
            }
        }

        if (cmd.hasOption("hdp2") && this.getName().equalsIgnoreCase("u3")) {
            String[] includeIds = {"1", "2", "3", "4", "5"};
            List<String> includes = Arrays.asList(includeIds);
            for (SreProcessBase proc : procContainer.getProcesses()) {
                if (includes.contains(proc.getId())) {
                    proc.setActive(Boolean.TRUE);
                    proc.setSkip(Boolean.FALSE);
                    if (proc.getId().equals("3")) {
                        ((DbSetProcess)proc).setCommandChecks(null);
                    }
                } else {
                    proc.setActive(Boolean.FALSE);
                    proc.setSkip(Boolean.TRUE);
                }
            }
        }

        if (cmd.hasOption("hdp3") && this.getName().equalsIgnoreCase("u3")) {
            String[] includeIds = {"1","5"};
            List<String> includes = Arrays.asList(includeIds);
            for (SreProcessBase proc : procContainer.getProcesses()) {
                if (includes.contains(proc.getId())) {
                    proc.setActive(Boolean.TRUE);
                    proc.setSkip(Boolean.FALSE);
                } else {
                    proc.setActive(Boolean.FALSE);
                    proc.setSkip(Boolean.TRUE);
                }
            }
        }

        // Initialize with config and output directory.
        String configFile = null;
        if (cmd.hasOption("cfg")) {
            configFile = cmd.getOptionValue("cfg");
        } else {
            configFile = System.getProperty("user.home") + System.getProperty("file.separator") + ".hive-sre/cfg/default.yaml";
        }
        outputDirectory = cmd.getOptionValue("o");
        // HERE: determine output dir and setup a place to write out the hsmm includelist config.
        if (cmd.hasOption("db")) {
            String[] dbsOverride = cmd.getOptionValues("db");
            outputDirectory = getProcessContainer().initToDBSet(configFile, outputDirectory, dbsOverride);
        } else if (cmd.hasOption("dbRegEx")) {
            outputDirectory = getProcessContainer().initToInclude(configFile, outputDirectory, cmd.getOptionValue("dbRegEx"));
        } else if (cmd.hasOption("edbRegEx")) {
            outputDirectory = getProcessContainer().initToExclude(configFile, outputDirectory, cmd.getOptionValue("edbRegEx"));
        } else {
            outputDirectory = getProcessContainer().initToNoFilters(configFile, outputDirectory);
        }

    }

    private Options getOptions() {

        // create Options object
        Options options = new Options();

        Option helpOption = new Option("h", "help", false,
                "Help");
        helpOption.setRequired(false);
        options.addOption(helpOption);

        Option outputOption = new Option("o", "output-dir", true,
                "Output Directory to save results from Sre.");
        outputOption.setRequired(false);
        options.addOption(outputOption);

        OptionGroup dbOptionGroup = new OptionGroup();
        Option dbOption = new Option("db", "database", true,
                "Comma separated list of Databases.  Will override config. (upto 100)");
        dbOption.setValueSeparator(',');
        dbOption.setArgs(100);
        dbOption.setRequired(false);
        dbOptionGroup.addOption(dbOption);

//        OptionGroup dbOptionGroup = new OptionGroup();
        Option edbOption = new Option("edbRegEx", "exclude-database-regex", true,
                "A RegEx that will filter OUT matching databases from processing");
        edbOption.setValueSeparator(',');
        edbOption.setArgs(100);
        edbOption.setRequired(false);
        dbOptionGroup.addOption(edbOption);

        Option dbRegExOption = new Option("dbRegEx", "database-regex", true,
                "A RegEx of databases to process");
        dbRegExOption.setRequired(false);
        dbOptionGroup.setRequired(false);
        dbOptionGroup.addOption(dbRegExOption);

        options.addOptionGroup(dbOptionGroup);

        OptionGroup procOptions = new OptionGroup();

        Option cdhOption = new Option("cdh", "cloudera-data-hub", false,
                "Run processes that make sense for CDH.");
        cdhOption.setRequired(false);
        procOptions.addOption(cdhOption);

        Option hdp2Option = new Option("hdp2", "hortonworks-data-platfrom-v2", false,
                "Run processes that make sense for HDP2.");
        hdp2Option.setRequired(false);
        procOptions.addOption(hdp2Option);

        Option hdp3Option = new Option("hdp3", "hortonworks-data-platfrom-v3", false,
                "Run processes that make sense for HDP3.");
        hdp3Option.setRequired(false);
        procOptions.addOption(hdp3Option);

        Option includeOption = new Option("i", "include", true,
                "Comma separated list of process id's to run.  When not specified, ALL processes are run.");
        includeOption.setValueSeparator(',');
        includeOption.setArgs(100);
        includeOption.setRequired(false);
        procOptions.addOption(includeOption);

        options.addOptionGroup(procOptions);

        Option cfgOption = new Option("cfg", "config", true,
                "Config with details for the Sre Job.  Must match the either sre or u3 selection. Default: $HOME/.hive-sre/cfg/default.yaml");
        cfgOption.setRequired(false);
        options.addOption(cfgOption);

        Option hfwOption = new Option("hfw", "hive-framework", true,
                "The custom HiveFramework check configuration. Needs to be in the 'Classpath'.");
        hfwOption.setRequired(false);
        options.addOption(hfwOption);

        Option sccOption = new Option("scc", "skip-command-checks", false,
                "Don't process the command checks for the process.");
        sccOption.setRequired(false);
        options.addOption(sccOption);

        return options;

    }

    @Override
    public String toString() {
        return "HiveFrameworkCheck{}";
    }
}
