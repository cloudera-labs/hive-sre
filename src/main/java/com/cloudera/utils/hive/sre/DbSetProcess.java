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

package com.cloudera.utils.hive.sre;

import com.cloudera.utils.hadoop.HadoopSession;
import com.cloudera.utils.hadoop.shell.command.CommandReturn;
import com.cloudera.utils.hive.config.HiveStrictManagedMigrationElements;
import com.cloudera.utils.hive.config.HiveStrictManagedMigrationIncludeListConfig;
import com.cloudera.utils.hive.reporting.CounterGroup;
import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.hive.reporting.TaskState;
import com.cloudera.utils.sql.JDBCUtils;
import com.cloudera.utils.sql.Parameter;
import com.cloudera.utils.sql.QueryDefinition;
import com.cloudera.utils.sql.ResultArray;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.validation.valueextraction.Unwrapping;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    private SkipCommandCheck skipCommandCheck;

    // HiveStrictManagedMigration Output Config
    private HiveStrictManagedMigrationElements hsmmElements;
    private String dbListingQuery;
    private Map<String, Parameter> dbListingParameters;
    private String listingQuery;
    private Map<String, Parameter> listingParameters;
    private String[] listingColumns;
    private String pathsListingQuery;
    private Map<String, Parameter> pathListingParameters;

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

    public SkipCommandCheck getSkipCommandCheck() {
        return skipCommandCheck;
    }

    public void setSkipCommandCheck(SkipCommandCheck skipCommandCheck) {
        this.skipCommandCheck = skipCommandCheck;
    }

    public String getDbListingQuery() {
        return dbListingQuery;
    }

    public void setDbListingQuery(String dbListingQuery) {
        this.dbListingQuery = dbListingQuery;
    }

    public Map<String, Parameter> getDbListingParameters() {
        return dbListingParameters;
    }

    public void setDbListingParameters(Map<String, Parameter> dbListingParameters) {
        this.dbListingParameters = dbListingParameters;
    }

    public String getListingQuery() {
        return listingQuery;
    }

    public void setListingQuery(String listingQuery) {
        this.listingQuery = listingQuery;
    }

    public Map<String, Parameter> getListingParameters() {
        return listingParameters;
    }

    public void setListingParameters(Map<String, Parameter> listingParameters) {
        this.listingParameters = listingParameters;
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

    public Map<String, Parameter> getPathListingParameters() {
        return pathListingParameters;
    }

    public void setPathListingParameters(Map<String, Parameter> pathListingParameters) {
        this.pathListingParameters = pathListingParameters;
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
        } else {
            if (getSkipCommandCheck() != null) {
                getSkipCommandCheck().successStream = this.success;
                getSkipCommandCheck().errorStream = this.error;
            }
        }
    }

    @Override
    public void init(ProcessContainer parent) throws FileNotFoundException {
        super.init(parent);
        initHeader();

        counterGroup = new CounterGroup(getUniqueName());

        // Add Report Counters.
        if (getCommandChecks() != null) {
            for (CommandReturnCheck crr : getCommandChecks()) {
                getParent().getReporter().addCounter(counterGroup, crr.getCounter());
            }
        } else {
            if (getSkipCommandCheck() != null) {
                getParent().getReporter().addCounter(counterGroup, getSkipCommandCheck().getCounter());
            }
        }
    }

    @Override
    public Boolean testSQLScript() {
        Boolean rtn = Boolean.TRUE;
        String targetQueryDef = this.dbListingQuery;
        QueryDefinition queryDefinition = getQueryDefinitions().getQueryDefinition(targetQueryDef);
        LOG.info("Testing DB SQL Definition: " + targetQueryDef);
        LOG.info("Testing DB SQL: " + queryDefinition.getStatement());
        try (Connection conn = getParent().getConnectionPools().getMetastoreDirectConnection()) {
            // build prepared statement for targetQueryDef
            PreparedStatement preparedStatement = JDBCUtils.getPreparedStatement(conn, queryDefinition);
            // apply any overrides from the user configuration.
            Map<String, Parameter> queryOverrides = getDbListingParameters();
            JDBCUtils.setPreparedStatementParameters(preparedStatement, queryDefinition, queryOverrides);
            // Run
            ResultSet check = preparedStatement.executeQuery();
            // Convert Result to an array
            ResultArray rarray = new ResultArray(check);
            // Close ResultSet
            check.close();
        } catch (SQLException e) {
            rtn = Boolean.FALSE;
            LOG.error("Test Failure for DB SQL Definition: " + targetQueryDef, e);
            error.println(targetQueryDef);
            error.println("> Processing Issue: " + e.getMessage());
            e.printStackTrace(error);
        } finally {
            LOG.info("Testing Complete for DB SQL Definition: " + targetQueryDef);
        }
        String targetPathQueryDef = this.pathsListingQuery;
        QueryDefinition queryPathDefinition = getQueryDefinitions().getQueryDefinition(targetPathQueryDef);
        LOG.info("Testing Path SQL Definition: " + targetPathQueryDef);
        LOG.info("Testing Path SQL: " + queryPathDefinition.getStatement());
        try (Connection conn = getParent().getConnectionPools().getMetastoreDirectConnection()) {
            // build prepared statement for targetQueryDef
            PreparedStatement preparedStatement = JDBCUtils.getPreparedStatement(conn, queryPathDefinition);
            // apply any overrides from the user configuration.
            Map<String, Parameter> queryOverrides = getPathListingParameters();
            JDBCUtils.setPreparedStatementParameters(preparedStatement, queryPathDefinition, queryOverrides);
            // Run
            ResultSet check = preparedStatement.executeQuery();
            // Convert Result to an array
            ResultArray rarray = new ResultArray(check);
            // Close ResultSet
            check.close();
        } catch (SQLException e) {
            rtn = Boolean.FALSE;
            LOG.error("Test Failure for Path SQL Definition: " + targetQueryDef, e);
            error.println(targetQueryDef);
            error.println("> Processing Issue: " + e.getMessage());
            e.printStackTrace(error);
        } finally {
            LOG.info("Testing Complete for Path SQL Definition: " + targetQueryDef);
        }
        setInitializing(Boolean.FALSE);
        return rtn;
    }

    protected void doIt() {
        // Run the dbListing Route
        if (getDbListingQuery() != null) {
            String[] dbs = null;
            if (getDbsOverride() != null && getDbsOverride().length > 0) {
                dbs = getDbsOverride();
            } else {
                try (Connection conn = getParent().getConnectionPools().getMetastoreDirectConnection()) {
                    LOG.info(this.getDisplayName());
                    String targetQueryDef = this.dbListingQuery;
                    // build prepared statement for targetQueryDef
                    QueryDefinition queryDefinition = getQueryDefinitions().getQueryDefinition(targetQueryDef);
                    PreparedStatement preparedStatement = JDBCUtils.getPreparedStatement(conn, queryDefinition);
                    // apply any overrides from the user configuration.
                    Map<String, Parameter> queryOverrides = getDbListingParameters();
                    JDBCUtils.setPreparedStatementParameters(preparedStatement, queryDefinition, queryOverrides);
                    // Run
                    ResultSet check = preparedStatement.executeQuery();
                    // Convert Result to an array
                    ResultArray rarray = new ResultArray(check);
                    if (getIncludeRegEx() != null) {
                        LOG.info(getDisplayName() + " will include DB(s) that 'match' RegEx: " + getIncludeRegEx());
                        rarray.keep(getIncludeRegEx(), 0);
                    } else if (getExcludeRegEx() != null) {
                        LOG.info(getDisplayName() + " will include DB(s) that do 'NOT' match RegEx: " + getExcludeRegEx());
                        rarray.remove(getExcludeRegEx(), 0);
                    }
                    // Close ResultSet
                    check.close();
                    // Close PreparedStatement
                    preparedStatement.close();
                    // build array of tables.
                    dbs = rarray.getColumn("name");
                    System.out.println(getDisplayName() + " - found " + dbs.length + " databases to process.");
                    StringBuilder sb = new StringBuilder();
                    for (String db : dbs) {
                        sb.append(db).append(";");
                    }
                    LOG.info(getDisplayName() + " will process DB(s): " + sb.toString());
                } catch (SQLException e) {
                    throw new RuntimeException("Issue getting 'databases' to process.", e);
                }
            }

            // Build an Element Path for each database.  This will be use to divide the work.
            int i = 0;
            counterGroup.addAndGetTaskState(TaskState.CONSTRUCTED, dbs.length);
            for (String database : dbs) {
                DbPaths paths = new DbPaths(database, this);
                paths.setCommandChecks(this.getCommandChecks());
                paths.setSkipCommandCheck(this.getSkipCommandCheck());
                paths.setCounterGroup(counterGroup);
                if (paths.init() || this.getCommandChecks() == null) {
                } else {
                    System.err.println("Issue establishing a connection to HDFS.  " +
                            "Check credentials(kerberos), configs(/etc/hadoop/conf), " +
                            "and/or availability of the HDFS service. " +
                            "Can you run an 'hdfs' cli command successfully?");
                    System.exit(-1);
                }
                i++;
                LOG.info(getDisplayName() + " adding paths for db: " + database);
                // Add Runnable to Main ThreadPool
                Future<String> sf = getParent().getTaskThreadPool().submit(paths);
            }

            if (getCommandChecks() == null) {
                if (getSkipCommandCheck() != null) {
//                this.success.println("-- Basic Processing (Skip Command Check specified)");
                    if (getSkipCommandCheck().getTitle() != null) {
                        this.success.println(ReportingConf.substituteVariables(getSkipCommandCheck().getTitle()));
                    }
                    if (getSkipCommandCheck().getNote() != null) {
                        this.success.println(getSkipCommandCheck().getNote());
                    }
                } else {
                    this.success.println("Command Checks Skipped.  Rules Processing Skipped.");
                }
            }
        } else if (getListingQuery() != null) {
            // run the listing route.
            HadoopSession cli = null;

            try (Connection conn = getParent().getConnectionPools().getMetastoreDirectConnection()) {
                LOG.info(this.getDisplayName());
                // Set the Counter to ensure we don't get kicked out due to inactivity.
                counterGroup.addAndGetTaskState(TaskState.CONSTRUCTED, 1);
                String targetQueryDef = this.listingQuery;
                // build prepared statement for targetQueryDef
                QueryDefinition queryDefinition = getQueryDefinitions().getQueryDefinition(targetQueryDef);
                PreparedStatement preparedStatement = JDBCUtils.getPreparedStatement(conn, queryDefinition);
                // apply any overrides from the user configuration.
                Map<String, Parameter> queryOverrides = getListingParameters();
                JDBCUtils.setPreparedStatementParameters(preparedStatement, queryDefinition, queryOverrides);
                // Run
                ResultSet check = preparedStatement.executeQuery();
                // Convert Result to an array
                ResultArray rarray = new ResultArray(check);

                check.close();
                preparedStatement.close();

                String[] columns = getListingColumns();
                LOG.info("Columns: " + Arrays.toString(columns));
                String[][] columnsArray = rarray.getColumns(columns);

                if (getCommandChecks() == null) {
                    if (getSkipCommandCheck() != null) {
//                this.success.println("-- Basic Processing (Skip Command Check specified)");
                        if (getSkipCommandCheck().getTitle() != null) {
                            this.success.println(ReportingConf.substituteVariables(getSkipCommandCheck().getTitle()));
                        }
                        if (getSkipCommandCheck().getNote() != null) {
                            this.success.println(getSkipCommandCheck().getNote());
                        }
                    } else {
                        this.success.println("Command Checks Skipped.  Rules Processing Skipped.");
                    }
                }

                if (getCommandChecks() != null) {
                    cli = getParent().getCliPool().borrow();
                }

                if (cli == null && getCommandChecks() != null) {
                    System.err.println("Issue getting dfs client session. Check configurations for DFS.");
                    System.exit(-1);
                }

                /*
                Integer[] hsmmElementLoc = null;
                HiveStrictManagedMigrationElements hsmmElements = getParent().getHsmmElements();
                // If we found an hsmmelement attribute, populate the location parts
                // so we can add the reference for the hsmm processing config.
                if (hsmmElements != null) {
                    hsmmElementLoc = new Integer[2];
                    // Align the locations in the array with the names
                    for (int i = 0;i < columns.length;i++) {
                        if (columns[i].equals(hsmmElements.getDatabaseField())) {
                            hsmmElementLoc[0] = i;
                        }
                        if (columns[i].equals(hsmmElements.getTableField())) {
                            hsmmElementLoc[1] = i;
                        }
                    }
                    // If we didn't find both, then set to null.
                    if (hsmmElementLoc[0] == null || hsmmElementLoc[1] == null) {
                        // TODO: Need to throw config exception in this condition.
                        hsmmElementLoc = null;
                    }
                }
                 */
                // Loop through the listing
                if (columnsArray != null && columnsArray[0] != null && columnsArray[0].length > 0) {
                    String[] args = new String[columnsArray.length];
                    for (int i = 0; i < columnsArray[0].length; i++) { //String path : columnArray) {
//                    String[] args = new String[columnsArray.length];
                        for (int a = 0; a < columnsArray.length; a++) {
                            if (columnsArray[a][i] != null)
                                args[a] = columnsArray[a][i];
                            else
                                args[a] = " "; // Prevent null in array.  Messes up String.format when array has nulls.
                        }
//                        if (hsmmElementLoc != null) {
//                            // When defined, add elements to hsmm.
//                            HiveStrictManagedMigrationIncludeListConfig hsmmwcfg =
//                                    HiveStrictManagedMigrationIncludeListConfig.getInstance();
//                            hsmmwcfg.addTable(args[hsmmElementLoc[0]], args[hsmmElementLoc[1]]);
//                        }
                        if (getCommandChecks() != null) {
                            for (CommandReturnCheck lclCheck : getCommandChecks()) {
                                try {
                                    LOG.info(getDisplayName() + ":" + lclCheck.getDisplayName() + " " + Arrays.toString(args));
                                    String rcmd = lclCheck.getFullCommand(args);
                                    if (rcmd != null) {
                                        CommandReturn cr = cli.processInput(rcmd);
//                                    lclCheck.getCounter().incCount(TaskState.PROCESSED, 1);
                                        if (!cr.isError() || (lclCheck.getInvertCheck() && cr.isError())) {
                                            lclCheck.onSuccess(cr, args);
                                            lclCheck.getCounter().incCount(TaskState.SUCCESS, 1);
                                        } else {
                                            lclCheck.onError(cr, args);
                                            lclCheck.getCounter().incCount(TaskState.ERROR, 1);
                                        }
                                    }
                                } catch (RuntimeException t) {
                                    // Malformed cli request.  Input is missing an element required to complete call.
                                    // Unusual, but not an expection.
                                    t.printStackTrace();
                                    throw t;
                                }
                            }
                        } else {
                            if (getSkipCommandCheck() != null) {
                                getSkipCommandCheck().onSuccess(args);
                                getSkipCommandCheck().getCounter().incCount(TaskState.SUCCESS, 1);
                            }
                        }
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException("Issue getting 'databases' to process.", e);
            }

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
        } else {
            // Check if option for default result is there.  IE: SkipCommandChecks...
            if (getSkipCommandCheck() != null) {

            }
        }
        return sb.toString();
    }

    @Override
    public String call() throws Exception {
        if (isTestSQL()) {
            testSQLScript();
        } else {
            doIt();
        }
        return "done";
    }


}
