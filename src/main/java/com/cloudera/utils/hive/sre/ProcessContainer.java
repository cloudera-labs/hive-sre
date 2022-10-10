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

package com.cloudera.utils.hive.sre;

import com.cloudera.utils.hive.config.Metastore;
import com.cloudera.utils.hive.config.SreProcessesConfig;
import com.cloudera.utils.hive.reporting.Reporter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.cloudera.utils.hadoop.HadoopSession;
import com.cloudera.utils.hadoop.HadoopSessionFactory;
import com.cloudera.utils.hadoop.HadoopSessionPool;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/*
The 'ProcessContainer' is the definition and runtime structure
 */
@JsonIgnoreProperties({"config", "reporter", "taskThreadPool", "procThreadPool", "processThreads", "cliPool",
        "connectionPools", "outputDirectory", "dbsOverride", "includeFilter", "excludeFilter", "testSQL"})
public class ProcessContainer implements Runnable {
    private static Logger LOG = LogManager.getLogger(ProcessContainer.class);

    private boolean initializing = Boolean.TRUE;
    private SreProcessesConfig config;
    private Reporter reporter;
    //    private ScheduledExecutorService threadPool;
    private ThreadPoolExecutor taskThreadPool;
    private ThreadPoolExecutor procThreadPool;

    private Thread reporterThread;

    private List<Future<String>> processThreads;
    private ConnectionPools connectionPools;
    private String outputDirectory;
    private List<Integer> includes = new ArrayList<Integer>();

    private HadoopSessionPool cliPool;

    private String[] dbsOverride = null;
    private String includeFilter = null;
    private String excludeFilter = null;

    private Boolean testSQL = Boolean.FALSE;

    public HadoopSessionPool getCliPool() {
        return cliPool;
    }

    public void setCliPool(HadoopSessionPool cliPool) {
        this.cliPool = cliPool;
    }

    /*
        The list of @link SreProcessBase instances to run in this container.
         */
    private List<SreProcessBase> processes = new ArrayList<SreProcessBase>();
    private int parallelism = 3;

    public SreProcessesConfig getConfig() {
        return config;
    }

    public void setConfig(SreProcessesConfig config) {
        this.config = config;
        this.reporter.setRefreshInterval(this.config.getReportingInterval());
    }

    public Reporter getReporter() {
        return reporter;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public void addInclude(Integer include) {
        includes.add(include);
    }

    public ThreadPoolExecutor getTaskThreadPool() {
        if (taskThreadPool == null) {
            taskThreadPool = new ThreadPoolExecutor(getConfig().getParallelism(), getConfig().getParallelism(),
                    5000l, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        }
        return taskThreadPool;
    }

    public ThreadPoolExecutor getProcThreadPool() {
        if (procThreadPool == null) {
            procThreadPool = new ThreadPoolExecutor(getConfig().getParallelism(), getConfig().getParallelism(),
                    5000l, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        }
        return procThreadPool;
    }

    public ConnectionPools getConnectionPools() {
        return connectionPools;
    }

    public void setConnectionPools(ConnectionPools connectionPools) {
        this.connectionPools = connectionPools;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public List<SreProcessBase> getProcesses() {
        return processes;
    }

    public void setProcesses(List<SreProcessBase> processes) {
        this.processes = processes;
    }

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    public Boolean isTestSQL() {
        return testSQL;
    }

    public void setTestSQL(Boolean testSQL) {
        this.testSQL = testSQL;
        for (SreProcessBase process : getProcesses()) {
            process.setTestSQL(this.testSQL);
        }
        reporter.setTestSQL(this.testSQL);
    }

    public boolean isInitializing() {
        return initializing;
    }

    public ProcessContainer() {
        this.reporter = new Reporter();
        this.reporter.setProcessContainer(this);
    }

    public Boolean isActive() {
        Boolean rtn = Boolean.FALSE;
        for (SreProcessBase proc : getProcesses()) {
            if (proc.isActive() && proc.isInitializing()) {
                rtn = Boolean.TRUE;
                break;
            }
        }
        if (this.getProcThreadPool().getActiveCount() > 0) {
            rtn = Boolean.TRUE;
        }
        if (this.getTaskThreadPool().getActiveCount() > 0) {
            rtn = Boolean.TRUE;
        }
        return rtn;
    }

    public void run() {
        initResources();
        int i = 0;
        while (true) {
            boolean check = true;
            try {
                Thread.sleep(1000);
                i++;
                if (i > 100) {
                    System.gc();
                    i = 0;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean procActive = isActive();
            if (procActive) {
                // Try again. This happens because we are editing the
                // list in the background.
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
        LOG.info("Shutting down Thread Pool.");
        getTaskThreadPool().shutdown();
        getProcThreadPool().shutdown();
        getCliPool().close();
        if (reporterThread != null) {
            reporterThread.interrupt();
        }
        for (SreProcessBase process : getProcesses()) {
            if (!process.isSkip()) {
                System.out.println(process.getUniqueName());
                System.out.println(process.getOutputDetails());
            }
        }
    }

    public String initToInclude(String config, String outputDirectory, String includeRegEx) {
        this.includeFilter = includeRegEx;
        this.excludeFilter = null;
        this.dbsOverride = null;
        return init(config, outputDirectory);
    }

    public String initToExclude(String config, String outputDirectory, String excludeRegEx) {
        this.includeFilter = null;
        this.excludeFilter = excludeRegEx;
        this.dbsOverride = null;
        return init(config, outputDirectory);
    }

    public String initToDBSet(String config, String outputDirectory, String[] dbsOverride) {
        this.includeFilter = null;
        this.excludeFilter = null;
        this.dbsOverride = dbsOverride;
        return init(config, outputDirectory);
    }

    public String initToNoFilters(String config, String outputDirectory) {
        this.includeFilter = null;
        this.excludeFilter = null;
        this.dbsOverride = null;
        return init(config, outputDirectory);
    }


    private void setFilter(SreProcessBase process) {
        if (dbsOverride != null) {
            process.setDbsOverride(dbsOverride);
        } else if (includeFilter != null) {
            process.setIncludeRegEx(includeFilter);
        } else if (excludeFilter != null) {
            process.setExcludeRegEx(excludeFilter);
        }
    }

    protected String init(String config, String outputDirectory) {
        initializing = Boolean.TRUE;
        String jobRunDir = null;
        if (config == null || outputDirectory == null) {
            throw new RuntimeException("Config File and Output Directory must be set before init.");
        } else {
            Date now = new Date();
            DateFormat df = new SimpleDateFormat("YY-MM-dd_HH-mm-ss");
            jobRunDir = outputDirectory + System.getProperty("file.separator") + df.format(now);
            setOutputDirectory(jobRunDir);
            File jobDir = new File(jobRunDir);
            if (!jobDir.exists()) {
                jobDir.mkdirs();
            }
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            File cfgFile = new File(config);
            if (!cfgFile.exists()) {
                throw new RuntimeException("Missing configuration file: " + config);
            } else {
                System.out.println("Using Config: " + config);
            }
            String yamlCfgFile = FileUtils.readFileToString(cfgFile, Charset.forName("UTF-8"));
            SreProcessesConfig sreConfig = mapper.readerFor(SreProcessesConfig.class).readValue(yamlCfgFile);
            sreConfig.validate();
            setConfig(sreConfig);

        } catch (
                IOException e) {
            throw new RuntimeException("Issue getting configs", e);
        }

        initializing = Boolean.FALSE;

        return jobRunDir;
    }

    protected void initResources() {
        try {
            this.connectionPools = new ConnectionPools(getConfig());
            this.connectionPools.init();

            GenericObjectPoolConfig<HadoopSession> hspCfg = new GenericObjectPoolConfig<HadoopSession>();
            hspCfg.setMaxTotal(getConfig().getParallelism() * 2);
            this.cliPool = new HadoopSessionPool(new GenericObjectPool<HadoopSession>(new HadoopSessionFactory(), hspCfg));
            if (this.cliPool == null) {
                throw new RuntimeException("Issue establishing DFS connections.  Check for kerberos ticket and/or dfs client configs");
            }
            // Needs to be added first, so it runs the reporter thread.
            reporterThread = new Thread(getReporter());

            for (SreProcessBase process : getProcesses()) {
                if (process.isActive()) {
                    setFilter(process);
                    // Set the dbType here.
                    if (getConfig().getMetastoreDirect().getType() != null) {
                        switch (getConfig().getMetastoreDirect().getType()) {
                            case MYSQL:
                                process.setDbType(Metastore.DB_TYPE.MYSQL);
                                break;
                            case ORACLE:
                                process.setDbType(Metastore.DB_TYPE.ORACLE);
                                break;
                            case POSTGRES:
                                process.setDbType(Metastore.DB_TYPE.POSTGRES);
                                break;
                            default:
                                System.err.println("Hasn't been implemented yet.");
                                throw new NotImplementedException();
                        }
                    }

                    // Check that it's still active after init.
                    // When there's nothing to process, it won't be active.
                    int delay = 100;
                    process.setParent(this);
                    process.setOutputDirectory(getOutputDirectory());
                    process.init(this);
                    if (testSQL) {
                        process.testSQLScript();
                    } else {
                        getProcThreadPool().submit(process);
                    }
                }
            }
            if (!isTestSQL()) {
                reporterThread.start();
            }
        } catch (
                IOException e) {
            throw new RuntimeException("Issue getting configs", e);
        }
    }

    @Override
    public String toString() {
        return "ProcessContainer{}";
    }
}
