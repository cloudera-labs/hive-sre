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
        "connectionPools", "outputDirectory"})
public class ProcessContainer implements Runnable {
    private static Logger LOG = LogManager.getLogger(ProcessContainer.class);

    private boolean initializing = Boolean.TRUE;
    private SreProcessesConfig config;
    private Reporter reporter;
    //    private ScheduledExecutorService threadPool;
    private ThreadPoolExecutor taskThreadPool;
    private ThreadPoolExecutor procThreadPool;

    private List<Future<String>> processThreads;
    private ConnectionPools connectionPools;
    private String outputDirectory;
    private List<Integer> includes = new ArrayList<Integer>();

    private HadoopSessionPool cliPool;

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
        for (SreProcessBase process : getProcesses()) {
            if (!process.isSkip()) {
                System.out.println(process.getUniqueName());
                System.out.println(process.getOutputDetails());
            }
        }
    }

    public String init(String config, String outputDirectory, String[] dbsOverride) {
//        String realizedOutputDir = null;
        initializing = Boolean.TRUE;
        String job_run_dir = null;
        if (config == null || outputDirectory == null) {
            throw new RuntimeException("Config File and Output Directory must be set before init.");
        } else {
            Date now = new Date();
            DateFormat df = new SimpleDateFormat("YY-MM-dd_HH-mm-ss");
            job_run_dir = outputDirectory + System.getProperty("file.separator") + df.format(now);
            File jobDir = new File(job_run_dir);
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

            this.connectionPools = new ConnectionPools(getConfig());
            this.connectionPools.init();

            GenericObjectPoolConfig<HadoopSession> hspCfg = new GenericObjectPoolConfig<HadoopSession>();
            hspCfg.setMaxTotal(sreConfig.getParallelism() * 2);
            this.cliPool = new HadoopSessionPool(new GenericObjectPool<HadoopSession>(new HadoopSessionFactory(), hspCfg));

            // Needs to be added first, so it runs the reporter thread.
            Thread reporterThread = new Thread(getReporter());

            for (SreProcessBase process : getProcesses()) {
                if (process.isActive()) {
                    process.setDbsOverride(dbsOverride);
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
                            case MSSQL:
                                System.err.println("MSSQL hasn't been implemented yet.");
                                throw new NotImplementedException();
                        }
                    }

                    // Check that it's still active after init.
                    // When there's nothing to process, it won't be active.
                    int delay = 100;
                    process.setParent(this);
                    process.setOutputDirectory(job_run_dir);
                    process.init(this);
//                    getProcessThreads().add(
                    getProcThreadPool().submit(process);
//                    getProcessThreads().add(getThreadPool().schedule(process, 100, MILLISECONDS));

                }
            }

            reporterThread.start();

        } catch (
                IOException e) {
            throw new RuntimeException("Issue getting configs", e);
        }

        initializing = Boolean.FALSE;

        return job_run_dir;
    }

    @Override
    public String toString() {
        return "ProcessContainer{}";
    }
}
