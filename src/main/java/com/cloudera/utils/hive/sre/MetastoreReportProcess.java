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

import com.cloudera.utils.hive.config.HiveStrictManagedMigrationElements;
import com.cloudera.utils.hive.config.HiveStrictManagedMigrationIncludeListConfig;
import com.cloudera.utils.hive.reporting.CounterGroup;
import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.hive.reporting.TaskState;
import com.cloudera.utils.sql.JDBCUtils;
import com.cloudera.utils.sql.Parameter;
import com.cloudera.utils.sql.QueryDefinition;
import com.cloudera.utils.sql.ResultArray;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MetastoreReportProcess extends MetastoreProcess {
    private static Logger LOG = LogManager.getLogger(MetastoreReportProcess.class);

    private List<MetastoreQuery> metastoreQueryDefinitions = new ArrayList<MetastoreQuery>();
    private ScriptEngine scriptEngine = null;

    @Override
    public void init(ProcessContainer parent) throws FileNotFoundException {
        super.init(parent);

        counterGroup = new CounterGroup(getUniqueName());

        counterGroup.addAndGetTaskState(TaskState.CONSTRUCTED, getMetastoreQueryDefinitions().size());
        getParent().getReporter().addCounter(counterGroup, null);
        // Add Report Counters.
//        for (MetastoreQuery mq : getMetastoreQueryDefinitions()) {
//            getParent().getReporter().addCounter(counterGroup, crr.getCounter());
//        }

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

    @Override
    public Boolean testSQLScript() {
        Boolean rtn = Boolean.TRUE;

        for (MetastoreQuery metastoreQueryDefinition: getMetastoreQueryDefinitions()) {
            String[][] metastoreRecords = null;
            String targetQueryDef = metastoreQueryDefinition.getQuery();
            try (Connection conn = getParent().getConnectionPools().getMetastoreDirectConnection()) {
                LOG.info("Testing SQL Definition: " + targetQueryDef);
                // build prepared statement for targetQueryDef
                QueryDefinition queryDefinition = getQueryDefinitions().getQueryDefinition(targetQueryDef);
                LOG.info("Testing SQL: " + queryDefinition.getStatement());
                PreparedStatement preparedStatement = JDBCUtils.getPreparedStatement(conn, queryDefinition);
                // apply any overrides from the user configuration.
                Map<String, Parameter> queryOverrides = metastoreQueryDefinition.getParameters();
                JDBCUtils.setPreparedStatementParameters(preparedStatement, queryDefinition, queryOverrides);
                // Run
                ResultSet rCheck = preparedStatement.executeQuery();
                // Convert Result to an array
                ResultArray rarray = new ResultArray(rCheck);
                // Close ResultSet
                rCheck.close();
                // build array of columns
                String[] columns = metastoreQueryDefinition.getListingColumns();

                // TODO: Check Columns match rarray?

            } catch (SQLException e) {
                rtn = Boolean.FALSE;
                error.println(metastoreQueryDefinition.getQuery());
                LOG.error("Test Failure for SQL Definition: " + targetQueryDef, e);
                error.println("> Processing Issue: " + e.getMessage());
                e.printStackTrace(error);
            } catch (RuntimeException rte) {
                rtn = Boolean.FALSE;
                error.println(metastoreQueryDefinition.getQuery());
                LOG.error("Test Failure for SQL Definition: " + targetQueryDef, rte);
                error.println("> Processing Issue: " + rte.getMessage());
                rte.printStackTrace(error);
            } finally {
                LOG.info("Testing Complete for SQL Definition: " + targetQueryDef);
            }
        }
        setInitializing(Boolean.FALSE);
        return rtn;
    }

    public void doIt() {
        ScriptEngineManager sem = new ScriptEngineManager();
        scriptEngine = sem.getEngineByName("nashorn");

        success.println(ReportingConf.substituteVariables(getTitle()));

        if (getNote() != null)
            success.println(getNote());

        if (getHeader() != null)
            success.println(getHeader());
        LOG.info(this.getDisplayName());
//        this.setTotalCount(getMetastoreQueryDefinitions().size());
        for (MetastoreQuery metastoreQueryDefinition: getMetastoreQueryDefinitions()) {
            String[][] metastoreRecords = null;
            try (Connection conn = getParent().getConnectionPools().getMetastoreDirectConnection()) {
                String targetQueryDef = metastoreQueryDefinition.getQuery();
                LOG.info("Query Definition: " + targetQueryDef);
                // build prepared statement for targetQueryDef
                QueryDefinition queryDefinition = getQueryDefinitions().getQueryDefinition(targetQueryDef);
                LOG.info("Query Statement: " + queryDefinition.getStatement());
                PreparedStatement preparedStatement = JDBCUtils.getPreparedStatement(conn, queryDefinition);
                // apply any overrides from the user configuration.
                Map<String, Parameter> queryOverrides = metastoreQueryDefinition.getParameters();
                JDBCUtils.setPreparedStatementParameters(preparedStatement, queryDefinition, queryOverrides);
                // Run
                ResultSet rCheck = preparedStatement.executeQuery();
                // Convert Result to an array
                ResultArray rarray = new ResultArray(rCheck);
                // Close ResultSet
                rCheck.close();
                // build array of columns
                String[] columns = metastoreQueryDefinition.getListingColumns();

                metastoreRecords = rarray.getColumns(metastoreQueryDefinition.getListingColumns());

                if (metastoreRecords != null && metastoreRecords[0] != null && metastoreRecords[0].length > 0) {
                    if (metastoreQueryDefinition.getResultMessageHeader() != null) {
                        success.println(metastoreQueryDefinition.getResultMessageHeader());
                    }
                    if (metastoreQueryDefinition.getResultMessageDetailHeader() != null) {
                        success.println(metastoreQueryDefinition.getResultMessageDetailHeader());
                    }

                    Integer[] hsmmElementLoc = null;
                    HiveStrictManagedMigrationElements hsmmElements = metastoreQueryDefinition.getHsmmElements();
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

//                    setTotalCount(metastoreRecords[0].length);
                    for (int i = 0; i < metastoreRecords[0].length; i++) {
//                    incSuccess(1);
                        String[] record = new String[metastoreQueryDefinition.getListingColumns().length];
                        for (int j = 0; j < metastoreQueryDefinition.getListingColumns().length; j++) {
                            record[j] = metastoreRecords[j][i];
//                        serdeRecords[0][i], serdeRecords[1][i], serdeRecords[2][i]
                        }

                        if (hsmmElementLoc != null) {
                            // When defined, add elements to hsmm.
                            HiveStrictManagedMigrationIncludeListConfig hsmmwcfg =
                                    HiveStrictManagedMigrationIncludeListConfig.getInstance();
                            hsmmwcfg.addTable(record[hsmmElementLoc[0]], record[hsmmElementLoc[1]]);
                        }

                        // Use the Check OR the Result Message Template
                        if (metastoreQueryDefinition.getCheck() != null && metastoreQueryDefinition.getCheck().getTest() != null) {
                            // Params
                            List combined = new LinkedList(Arrays.asList(record));
                            // Configured Params
                            if (metastoreQueryDefinition.getCheck().getParams() != null)
                                combined.addAll(Arrays.asList(metastoreQueryDefinition.getCheck().getParams()));
                            try {
                                String testStr = String.format(metastoreQueryDefinition.getCheck().getTest(), combined.toArray());
                                Boolean checkTest = null;
                                checkTest = (Boolean) scriptEngine.eval(testStr);
                                if (checkTest) {
                                    if (metastoreQueryDefinition.getCheck().getPass() != null) {
                                        String passStr = String.format(metastoreQueryDefinition.getCheck().getPass(), combined.toArray());
                                        String passResult = (String) scriptEngine.eval(passStr);
                                        success.println(passResult);
//                                        sb.append(passResult).append("\n");
                                    }

                                } else {
                                    if (metastoreQueryDefinition.getCheck().getFail() != null) {
                                        String failStr = String.format(metastoreQueryDefinition.getCheck().getFail(), combined.toArray());
                                        String failResult = (String) scriptEngine.eval(failStr);
                                        success.println(failResult);
//                                        sb.append(failResult).append("\n");
                                    }
                                }
                            } catch (ScriptException e) {
                                e.printStackTrace();
                                System.err.println("Issue with script eval: " + this.getDisplayName());
                            } catch (MissingFormatArgumentException mfa) {
                                mfa.printStackTrace();
                                System.err.println("Bad Argument Match up for PATH check rule: " + this.getDisplayName());
                            }
                        } else {
                            if (metastoreQueryDefinition.getResultMessageDetailTemplate() != null) {
                                String message = String.format(metastoreQueryDefinition.getResultMessageDetailTemplate(), record);
                                success.println(message);
                            }
                        }
                    }
                } else {
                    if (metastoreQueryDefinition.getResultMessageHeader() != null) {
                        success.println(metastoreQueryDefinition.getResultMessageHeader());
                    }
                    success.println("\n> **Results empty**\n");
                }
                counterGroup.addAndGetTaskState(TaskState.PROCESSED, 1);
            } catch (SQLException e) {
                counterGroup.addAndGetTaskState(TaskState.ERROR, 1);
                error.println(metastoreQueryDefinition.getQuery());
                error.println("> Processing Issue: " + e.getMessage());
                e.printStackTrace(error);
            } catch (RuntimeException rte) {
                counterGroup.addAndGetTaskState(TaskState.ERROR, 1);
                error.println(metastoreQueryDefinition.getQuery());
                error.println("> Processing Issue: " + rte.getMessage());
                rte.printStackTrace(error);
            }
        }
        setInitializing(Boolean.FALSE);
    }


    public List<MetastoreQuery> getMetastoreQueryDefinitions() {
        return metastoreQueryDefinitions;
    }

    public void setMetastoreQueryDefinitions(List<MetastoreQuery> metastoreQueryDefinitions) {
        this.metastoreQueryDefinitions = metastoreQueryDefinitions;
    }

    @Override
    public String toString() {
        return "MetastoreReportProcess{}";
    }
}
