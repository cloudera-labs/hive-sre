/*
 * Copyright (c) 2023. Cloudera, Inc. All Rights Reserved
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

package com.cloudera.framework.sqlreports;

import com.cloudera.utils.Protect;
import com.cloudera.utils.SreSubApp;
import com.cloudera.utils.hive.config.SreProcessesConfig;
import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.hive.sre.ConnectionPools;
import com.cloudera.utils.hive.sre.MessageCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

public class DBAAnalysisFramework implements SreSubApp {

    private static final Logger LOG = LogManager.getLogger(DBAAnalysisFramework.class);
    private final String USAGE_CMD = "hive-sre dba ";
    public static final String QUERY_DEFS_RESOURCE = "dba_defs.yaml";
    public static final String NAV_TREE_RESOURCE = "dba_nav_tree.yaml";

    private com.cloudera.framework.sqlreports.render.CommandLine renderer = null;

    private SreProcessesConfig config;

    public SreProcessesConfig getConfig() {
        return config;
    }

    public void setConfig(SreProcessesConfig config) {
        this.config = config;
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

        Option tocOption = new Option("toc", "table-of-contents", false,
                "Table-of-Contents");
        tocOption.setRequired(false);
        procOptions.addOption(tocOption);

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

        Option pKeyOption = new Option("pkey", "password-key", true,
                "The key used to encrypt / decrypt the cluster jdbc passwords.  If not present, the passwords will be processed as is (clear text) from the config file.");
        pKeyOption.setRequired(false);
        pKeyOption.setArgName("password-key");
        options.addOption(pKeyOption);

        return options;
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
            SessionContext.getInstance().setQueryStore(sreConfig.getQueryAnalysis());
            SessionContext.getInstance().setConnectionPools(new ConnectionPools(sreConfig));
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

        // Initialize Connection Pool for query_analysis
        if (rtn) {
            // Initialize query definitions
            String defResource = "/dba/" + QUERY_DEFS_RESOURCE;
            URL configDefsURL = this.getClass().getResource(defResource);
            if (configDefsURL != null) {
                String yamlConfigDefinition = null;
                try {
                    yamlConfigDefinition = IOUtils.toString(configDefsURL);
                } catch (IOException e) {
                    throw new RuntimeException("Issue converting config: " + defResource, e);
                }
                try {
                    SessionContext.getInstance().setQueryResource(
                            mapper.readerFor(QueryResource.class).readValue(yamlConfigDefinition));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Issue deserializing config: " + defResource, e);
                }
            } else {
                throw new RuntimeException("Couldn't locate 'dba-Query Definitions': " +
                        configDefsURL.toString());
            }
        }

        if (rtn) {
            // Initialize navigation tree
            String navResource = "/dba/" + NAV_TREE_RESOURCE;
            URL configNavURL = this.getClass().getResource(navResource);
            if (configNavURL != null) {
                String yamlConfigDefinition = null;
                try {
                    yamlConfigDefinition = IOUtils.toString(configNavURL);
                } catch (IOException e) {
                    throw new RuntimeException("Issue converting config: " + navResource, e);
                }
                try {
                    SessionContext.getInstance().setNavigationTree(
                            mapper.readerFor(NavigationTree.class).readValue(yamlConfigDefinition));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Issue deserializing config: " + navResource, e);
                }
            } else {
                throw new RuntimeException("Couldn't locate 'dba-Navigation Tree': " +
                        configNavURL.toString());
            }
            rtn = SessionContext.getInstance().getNavigationTree().loadResources();
        }

        // Will validate the resources.
        rtn = SessionContext.getInstance().getQueryResource().loadResources();

        if (cmd.hasOption("toc")) {
            System.out.println(SessionContext.getInstance().getNavigationTree().showTOC());
            System.exit(0);
        }

        renderer = new com.cloudera.framework.sqlreports.render.CommandLine(getOptions());
        renderer.init(args);

        return rtn;
    }

    @Override
    public void start() {
        renderer.run();
    }

    @Override
    public void report() {

    }

    @Override
    public String getName() {
        return "DBA-Query Analysis";
    }
}
