package com.cloudera.utils.hive.sre;

import com.cloudera.utils.hive.reporting.CounterGroup;
import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.hive.reporting.TaskState;
import com.cloudera.utils.sql.QueryDefinition;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MetastoreActionProcess extends MetastoreProcess {

    private static Logger LOG = LogManager.getLogger(MetastoreActionProcess.class);

    // Specify whether any failure would trigger a rollback of the changes. Setting this to true will start a
    //     transaction before the first statement.  After all statements have completed, they will be committed.
    //     If any of the statements fail, a rollback would be issued. (requires db and driver support).
    private Boolean transactional = Boolean.TRUE;
    private String actionDefinition;

    @Override
    public void init(ProcessContainer parent) throws FileNotFoundException {
        super.init(parent);
        LOG.info("Init: " + getId());
        counterGroup = new CounterGroup(getUniqueName());
        QueryDefinition queryDefinition = getQueryDefinitions().getQueryDefinition(actionDefinition);
        counterGroup.addAndGetTaskState(TaskState.CONSTRUCTED, queryDefinition.getPreparedStatements().size());
        getParent().getReporter().addCounter(counterGroup, null);
        LOG.info("Init (done): " + getId());
    }

    @Override
    public String call() throws Exception {
        if (isTestSQL()) {
            testSQLScript();
        } else {
            doIt();
        }
        return "done" ;
    }

    public void doIt() {
        // Get a connection
        Connection conn = null;
        LOG.info("doIt: " + getId());
        success.println(ReportingConf.substituteVariables(getTitle()));
        if (getNote() != null)
            success.println(getNote());

        if (getHeader() != null)
            success.println(getHeader());
        LOG.info(this.getDisplayName());

        try {
            LOG.info("doIt (getting connection): " + getId());
            conn = getParent().getConnectionPools().getMetastoreDirectConnection();
            LOG.info("doIt (connection made): " + getId());
            QueryDefinition queryDefinition = getQueryDefinitions().getQueryDefinition(actionDefinition);
            //
            if (queryDefinition.getPreparedStatements() == null || queryDefinition.getPreparedStatements().size() < 1) {
                // This doesn't have what we're looking for.  It needs some preparedStatement definitions.
                error.println("No prepared statements found.");
                throw new RuntimeException("missing preparedStatements.");
            } else {
                if (transactional) {
                    conn.setAutoCommit(Boolean.FALSE);
                    LOG.info("Transaction (Start): " + getId());
                }
                // We should be looking at the prepared statements.
                for (String action : queryDefinition.getPreparedStatements().keySet()) {
                    String actionSql = queryDefinition.getPreparedStatements().get(action);
                    LOG.info("SQL for : " + action + ": " + getId() + " -> " + actionSql);
                    LOG.info("Statement (Preparing) for: " + action + " " + getId());
                    PreparedStatement preparedStatement = conn.prepareStatement(actionSql);
                    // TODO: Check for Parameters..
                    LOG.info("Statement (Running) for: " + action + " " + getId());
                    Long recordAffected = preparedStatement.executeLargeUpdate();
                    LOG.info("Statement (Completed) for: " + action + " " + getId());
                    success.println("Action for: " + action + " affected " + recordAffected + " records.");
                    // TODO: Record the number of records affected.
                    LOG.info("The query: " + action + " affected " + recordAffected + " records.");
                    counterGroup.addAndGetTaskState(TaskState.PROCESSED, 1);
                }
                if (transactional) {
                    LOG.info("Committing Transaction: " + getId());
                    conn.commit();
                    LOG.info("CommittED Transaction: " + getId());
                }
            }
        } catch (SQLException e) {
            if (transactional && conn != null) {
                try {
                    LOG.error("Issue, rollback started: " + getId(), e);
                    conn.rollback();
                    LOG.error("Issue, rollback complete: " + getId(), e);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            counterGroup.addAndGetTaskState(TaskState.ERROR, 1);
            throw new RuntimeException(e);
        }
        LOG.info("doIt (done): " + getId());
        setInitializing(Boolean.FALSE);
        setActive(Boolean.FALSE);
    }

    public String getActionDefinition() {
        return actionDefinition;
    }

    public void setActionDefinition(String actionDefinition) {
        this.actionDefinition = actionDefinition;
    }

    public Boolean getTransactional() {
        return transactional;
    }

    public void setTransactional(Boolean transactional) {
        this.transactional = transactional;
    }

    @Override
    public Boolean testSQLScript() {
        Boolean rtn = Boolean.TRUE;

        LOG.info("Testing SQL Definition: " + actionDefinition);
        // build prepared statement for targetQueryDef
        QueryDefinition queryDefinition = getQueryDefinitions().getQueryDefinition(actionDefinition);
        //
        if (queryDefinition.getPreparedStatements() == null || queryDefinition.getPreparedStatements().size() < 1) {
            // This doesn't have what we're looking for.  It needs some preparedStatement definitions.
            rtn = Boolean.FALSE;
        } else {
            // We should be looking at the prepared statements.
            for (String action : queryDefinition.getPreparedStatements().keySet()) {
                LOG.info("SQL for : " + action);
                LOG.info(queryDefinition.getPreparedStatements().get(action));
            }
        }
        setInitializing(Boolean.FALSE);
        return rtn;
    }

    @Override
    public String toString() {
        return "MetastoreActionProcess{}" ;
    }

}
