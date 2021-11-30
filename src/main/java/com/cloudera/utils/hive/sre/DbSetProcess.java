package com.cloudera.utils.hive.sre;

import com.cloudera.utils.hive.config.HiveStrictManagedMigrationElements;
import com.cloudera.utils.hive.reporting.CounterGroup;
import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.hive.reporting.TaskState;
import com.cloudera.utils.sql.JDBCUtils;
import com.cloudera.utils.sql.QueryDefinition;
import com.cloudera.utils.sql.ResultArray;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;


@JsonIgnoreProperties({"parent", "counterGroup", "config", "metastoreDirectDataSource", "h2DataSource",
        "outputDirectory", "dbPaths", "cliSession", "success", "error"})
public class DbSetProcess extends SreProcessBase {
    private static Logger LOG = LogManager.getLogger(DbSetProcess.class);

    private ProcessContainer parent;

    //    private List<DbPaths> dbPaths;
    private List<CommandReturnCheck> commandChecks;
    private CheckCalculation calculationCheck;

    // HiveStrictManagedMigration Output Config
    private HiveStrictManagedMigrationElements hsmmElements;
    private String dbListingQuery;
    private String[] listingColumns;
    private String pathsListingQuery;

    private List<ScheduledFuture<String>> pathsFutures = new ArrayList<ScheduledFuture<String>>();

    @Override
    public ProcessContainer getParent() {
        return parent;
    }

    @Override
    public void setParent(ProcessContainer parent) {
        this.parent = parent;
    }

    public List<CommandReturnCheck> getCommandChecks() {
        return commandChecks;
    }

    public void setCommandChecks(List<CommandReturnCheck> commandChecks) {
        this.commandChecks = commandChecks;
    }

    public CheckCalculation getCalculationCheck() {
        return calculationCheck;
    }

    public void setCalculationCheck(CheckCalculation calculationCheck) {
        this.calculationCheck = calculationCheck;
    }

    public String getDbListingQuery() {
        return dbListingQuery;
    }

    public void setDbListingQuery(String dbListingQuery) {
        this.dbListingQuery = dbListingQuery;
    }

    public String[] getListingColumns() {
        return listingColumns;
    }

    public void setListingColumns(String[] listingColumns) {
        this.listingColumns = listingColumns;
    }

    public String getPathsListingQuery() {
        return pathsListingQuery;
    }

    public void setPathsListingQuery(String pathsListingQuery) {
        this.pathsListingQuery = pathsListingQuery;
    }

    public HiveStrictManagedMigrationElements getHsmmElements() {
        return hsmmElements;
    }

    public void setHsmmElements(HiveStrictManagedMigrationElements hsmmElements) {
        this.hsmmElements = hsmmElements;
    }

    protected void initHeader() {
        if (getTitle() != null)
            this.success.println(ReportingConf.substituteVariables(getTitle()));
        if (getNote() != null)
            this.success.println(ReportingConf.substituteVariables(getNote()));
        if (getHeader() != null)
            this.success.println(ReportingConf.substituteVariables(getHeader()));

        if (getCommandChecks() != null) {
            for (CommandReturnCheck check : getCommandChecks()) {
                if (getTitle() != null) {
                    check.successStream.println(ReportingConf.substituteVariables(getTitle()));
                    check.errorStream.println(ReportingConf.substituteVariables(getTitle()));
                }
                if (getNote() != null) {
                    check.successStream.println(getNote());
                    check.errorStream.println(getNote());
                }
                if (getHeader() != null) {
                    check.successStream.println(getHeader());
                    check.errorStream.println(getHeader());
                }

                // If details for stream output are available in the check definition.
                // Set the Header if defined.
                if (check.getInvertCheck() && check.getTitle() != null) {
                    if (check.getProcessOnError()) {
                        check.errorStream.println(ReportingConf.substituteVariables(check.getTitle()));
                    }
                    if (check.getProcessOnSuccess()) {
                        check.successStream.println(ReportingConf.substituteVariables(check.getTitle()));
                    }
                }
                if (check.getInvertCheck() && check.getNote() != null) {
                    if (check.getProcessOnError()) {
                        check.errorStream.println(check.getNote());
                    }
                    if (check.getProcessOnSuccess()) {
                        check.successStream.println(check.getNote());
                    }
                }
                if (check.getInvertCheck() && check.getHeader() != null) {
                    if (check.getProcessOnError()) {
                        check.errorStream.println(check.getHeader());
                    }
                    if (check.getProcessOnSuccess()) {
                        check.successStream.println(check.getHeader());
                    }
                }

                // TODO: Validate inversion.
                if (!check.getInvertCheck() && check.getTitle() != null) {
                    if (check.getProcessOnError()) {
                        check.errorStream.println(ReportingConf.substituteVariables(check.getTitle()));
                    }
                    if (check.getProcessOnSuccess()) {
                        check.successStream.println(ReportingConf.substituteVariables(check.getTitle()));
                    }
                }
                if (!check.getInvertCheck() && check.getNote() != null) {
                    if (check.getProcessOnError()) {
                        check.errorStream.println(check.getNote());
                    }
                    if (check.getProcessOnSuccess()) {
                        check.successStream.println(check.getNote());
                    }
                }
                if (!check.getInvertCheck() && check.getHeader() != null) {
                    if (check.getProcessOnError()) {
                        check.errorStream.println(check.getHeader());
                    }
                    if (check.getProcessOnSuccess()) {
                        check.successStream.println(check.getHeader());
                    }
                }
            }
        }

    }

    @Override
    public void setOutputDirectory(String outputDirectory) throws FileNotFoundException {
        // Allow each Check to have its own output stream.
        super.setOutputDirectory(outputDirectory);
        if (getCommandChecks() != null) {
            for (CommandReturnCheck check : getCommandChecks()) {
                // If details for stream output are available in the check definition.
                if (check.getErrorFilename() != null) {
                    check.errorStream = outputFile(outputDirectory + System.getProperty("file.separator") + check.getErrorFilename());
                } else {
                    check.errorStream = this.error;
                }
                if (check.getSuccessFilename() != null) {
                    check.successStream = outputFile(outputDirectory + System.getProperty("file.separator") + check.getSuccessFilename());
                } else {
                    check.successStream = this.success;
                }
            }
        }
    }

    @Override
    public void init(ProcessContainer parent) throws FileNotFoundException {
        super.init(parent);
        initHeader();

        counterGroup = new CounterGroup(getUniqueName());

        // Add Report Counters.
        for (CommandReturnCheck crr : getCommandChecks()) {
            getParent().getReporter().addCounter(counterGroup, crr.getCounter());
        }
    }

    protected void doIt() {
        String[] dbs = null;
        if (getDbsOverride() != null && getDbsOverride().length > 0) {
            dbs = getDbsOverride();
        } else {
            try (Connection conn = getParent().getConnectionPools().getMetastoreDirectConnection()) {
                String targetQueryDef = this.dbListingQuery;
                // build prepared statement for targetQueryDef
                QueryDefinition queryDefinition = getQueryDefinitions().getQueryDefinition(targetQueryDef);
                PreparedStatement preparedStatement = JDBCUtils.getPreparedStatement(conn, queryDefinition);
                // apply any overrides from the user configuration.
                QueryDefinition queryOverride = getQueryOverride(targetQueryDef);
                JDBCUtils.setPreparedStatementParameters(preparedStatement, queryDefinition, queryOverride);
                // Run
                ResultSet check = preparedStatement.executeQuery();
                // Convert Result to an array
                ResultArray rarray = new ResultArray(check);
                // Close ResultSet
                check.close();
                // build array of tables.
                dbs = rarray.getColumn("name");
                System.out.println(getDisplayName() + " - found " + dbs.length + " databases to process.");
            } catch (SQLException e) {
                throw new RuntimeException("Issue getting 'databases' to process.", e);
            }
        }

        // Build an Element Path for each database.  This will be use to divide the work.
//        int threadCount = getParent().getConfig().getParallelism();


//        List<ReportCounter> counters = new ArrayList<ReportCounter>();
        int i = 0;
        counterGroup.addAndGetTaskState(TaskState.CONSTRUCTED, dbs.length);
        for (String database : dbs) {
            DbPaths paths = new DbPaths(database, this);
            paths.setCommandChecks(this.getCommandChecks());
            paths.setCounterGroup(counterGroup);
//            paths.error = this.error;
//            paths.success = this.success;
            if (paths.init()) {
//                paths.setStatus(CONSTRUCTED);
//                pathsList.add(paths);
            } else {
                throw new RuntimeException("Issue establishing a connection to HDFS.  " +
                        "Check credentials(kerberos), configs(/etc/hadoop/conf), " +
                        "and/or availability of the HDFS service. " +
                        "Can you run an 'hdfs' cli command successfully?");
            }

//            paths.setStatus(WAITING);
//            counters.add(paths.getCounter());
            i++;
            LOG.info("Adding paths for db: " + database);
//            getParent().getReporter().addCounter(getId() + ":" + getDisplayName(), paths.getCounter());
            // Add Runnable to Main ThreadPool
            Future<String> sf = getParent().getTaskThreadPool().submit(paths);
//            ScheduledFuture<String> sf = getParent().getThreadPool().schedule(paths, 10, MILLISECONDS);
//            getParent().addProcess(sf);
//            try {s
//                while (getParent().getProcessThreads().size() > 100) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException ie) {
//                        //
//                    }
//                }
//            } catch (ConcurrentModificationException cme) {
//                //
//            }
        }

        if (getCommandChecks() == null) {
            this.success.println("Command Checks Skipped.  Rules Processing Skipped.");
        }

        setInitializing(Boolean.FALSE);

    }

    @Override
    public String getOutputDetails() {
        String defaultReturnInfo = super.getOutputDetails();
        StringBuilder sb = new StringBuilder();
        if (defaultReturnInfo.length() > 0)
            sb.append(defaultReturnInfo).append("\n");
        if (getCommandChecks() != null) {
            for (CommandReturnCheck check : getCommandChecks()) {
                if (check.getSuccessFilename() != null) {
                    sb.append("\t" + check.getSuccessDescription() + " -> " + getOutputDirectory() + System.getProperty("file.separator") +
                            check.getSuccessFilename()).append("\n");
                }
                if (check.getErrorFilename() != null) {
                    sb.append("\t" + check.getErrorDescription() + " -> " + getOutputDirectory() + System.getProperty("file.separator") +
                            check.getErrorFilename());
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String call() throws Exception {
        doIt();
        return "done";
    }


}
