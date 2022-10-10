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
import com.cloudera.utils.hive.reporting.TaskState;
import com.cloudera.utils.sql.JDBCUtils;
import com.cloudera.utils.sql.QueryDefinition;
import com.cloudera.utils.sql.ResultArray;
import com.cloudera.utils.hadoop.HadoopSession;
import com.cloudera.utils.hadoop.shell.command.CommandReturn;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DbPaths extends SRERunnable {
    private static Logger LOG = LogManager.getLogger(DbPaths.class);

    private DbSetProcess parent;
    private CounterGroup counterGroup;

    private List<CommandReturnCheck> commandChecks = new ArrayList<CommandReturnCheck>();
    private SkipCommandCheck skipCommandCheck = null;

    private CheckCalculation calculationCheck = null;

    public DbSetProcess getParent() {
        return parent;
    }

    public void setParent(DbSetProcess parent) {
        this.parent = parent;
    }

    public void setCounterGroup(CounterGroup counterGroup) {
        this.counterGroup = counterGroup;
    }

    public List<CommandReturnCheck> getCommandChecks() {
        return commandChecks;
    }

    public void setCommandChecks(List<CommandReturnCheck> commandChecks) {
        this.commandChecks = commandChecks;
    }

    public SkipCommandCheck getSkipCommandCheck() {
        return skipCommandCheck;
    }

    public void setSkipCommandCheck(SkipCommandCheck skipCommandCheck) {
        this.skipCommandCheck = skipCommandCheck;
    }

    public CheckCalculation getCalculationCheck() {
        return calculationCheck;
    }

    public void setCalculationCheck(CheckCalculation calculationCheck) {
        this.calculationCheck = calculationCheck;
    }

    public DbPaths(String name, DbSetProcess dbSet) {
        setDisplayName(name);
        setParent(dbSet);
    }

    @Override
    public Boolean init() {
        Boolean rtn = Boolean.TRUE;
        if (getCommandChecks() != null) {
            try {
                HadoopSession cli = getParent().getParent().getCliPool().borrow();
                cli.processInput("ls");
                getParent().getParent().getCliPool().returnSession(cli);
            } catch (Exception t) {
                rtn = Boolean.FALSE;
            }
        }
        return rtn;
    }


    protected void doIt() {

        QueryDefinition queryDefinition = null;
        HadoopSession cli = null;

        try (Connection conn = getParent().getParent().getConnectionPools().
                getMetastoreDirectConnection()) {
            LOG.info(this.getDisplayName());
            queryDefinition = getParent().getQueryDefinitions().
                    getQueryDefinition(getParent().getPathsListingQuery());
            PreparedStatement preparedStatement = JDBCUtils.getPreparedStatement(conn, queryDefinition);

            Properties overrides = new Properties();
            overrides.setProperty("dbs", getDisplayName());
            JDBCUtils.setPreparedStatementParameters(preparedStatement, queryDefinition, overrides);

            ResultSet epRs = preparedStatement.executeQuery();
            ResultArray rarray = new ResultArray(epRs);
            epRs.close();

            epRs.close();
            preparedStatement.close();

            String[] columns = getParent().getListingColumns();
            LOG.info("Columns: " + Arrays.toString(columns));
            String[][] columnsArray = rarray.getColumns(columns);

            if (getCommandChecks() != null) {
                cli = getParent().getParent().getCliPool().borrow();
            }

            if (cli == null && getCommandChecks() != null) {
                System.err.println("Issue getting dfs client session. Check configurations for DFS.");
                System.exit(-1);
            }

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
            // Loop through the paths
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
                    if (hsmmElementLoc != null) {
                        // When defined, add elements to hsmm.
                        HiveStrictManagedMigrationIncludeListConfig hsmmwcfg =
                                HiveStrictManagedMigrationIncludeListConfig.getInstance();
                        hsmmwcfg.addTable(args[hsmmElementLoc[0]], args[hsmmElementLoc[1]]);
                    }
                    if (getCommandChecks() != null) {
                        for (CommandReturnCheck lclCheck : getCommandChecks()) {
                            try {
                                LOG.info(getParent().getDisplayName() + ":" + lclCheck.getDisplayName() + " " + Arrays.toString(args));
                                String rcmd = lclCheck.getFullCommand(args);
                                if (rcmd != null) {
                                    CommandReturn cr = cli.processInput(rcmd);
//                                    lclCheck.getCounter().incCount(TaskState.PROCESSED, 1);
                                    if (!cr.isError() || (lclCheck.getInvertCheck() && cr.isError())) {
                                        lclCheck.onSuccess(cr,args);
                                        lclCheck.getCounter().incCount(TaskState.SUCCESS, 1);
                                    } else {
                                        lclCheck.onError(cr,args);
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
            if (getCommandChecks() != null && getCommandChecks().size() > 0) {
                getCommandChecks().get(0).errorStream.println((queryDefinition != null) ? queryDefinition.getStatement() : "Unknown");
                getCommandChecks().get(0).errorStream.println("Failure in DbPaths" + e.getMessage());
            } else {
                error.println((queryDefinition != null) ? queryDefinition.getStatement() : "Unknown");
                error.println("Failure in DbPaths" + e.getMessage());
            }
            e.printStackTrace(error);
        } catch (Throwable t) {
            error.println("Failure in DbPaths:" + t.getMessage());
            t.printStackTrace(error);
//            System.exit(-1);
        } finally {
            if (cli != null) {
                getParent().getParent().getCliPool().returnSession(cli);
            }
            // When completed, increment the processed value.
            counterGroup.addAndGetTaskState(TaskState.PROCESSED, 1);
        }
    }

    @Override
    public String call() throws Exception {
        doIt();
        return "done";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbPaths dbPaths = (DbPaths) o;

        if (parent != null ? !parent.equals(dbPaths.parent) : dbPaths.parent != null) return false;
        if (counterGroup != null ? !counterGroup.equals(dbPaths.counterGroup) : dbPaths.counterGroup != null)
            return false;
        if (commandChecks != null ? !commandChecks.equals(dbPaths.commandChecks) : dbPaths.commandChecks != null)
            return false;
        return calculationCheck != null ? calculationCheck.equals(dbPaths.calculationCheck) : dbPaths.calculationCheck == null;
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (counterGroup != null ? counterGroup.hashCode() : 0);
        result = 31 * result + (commandChecks != null ? commandChecks.hashCode() : 0);
        result = 31 * result + (calculationCheck != null ? calculationCheck.hashCode() : 0);
        return result;
    }
}
