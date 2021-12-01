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

import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.sql.JDBCUtils;
import com.cloudera.utils.sql.QueryDefinition;
import com.cloudera.utils.sql.ResultArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetastoreQueryProcess extends MetastoreProcess {

    private MetastoreQuery metastoreQueryDefinition;

//    @Override
//    public void run() {
//        doIt();
//    }

    @Override
    public String call() throws Exception {
        doIt();
        return "done";
    }

    public void doIt() {
//        setStatus(PROCESSING);
        String[][] metastoreRecords = null;
//        this.setTotalCount(1);
        try (Connection conn = getParent().getConnectionPools().getMetastoreDirectConnection()) {
            String targetQueryDef = this.getMetastoreQueryDefinition().getQuery();
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
            // build array of columns
            metastoreRecords = rarray.getColumns(getMetastoreQueryDefinition().getListingColumns());
        } catch (SQLException e) {
//            incError(1);
            error.println(metastoreQueryDefinition.getQuery());
            error.println("> Processing Issue: " + e.getMessage());
//            setStatus(ERROR);
            return;
        }

        if (metastoreRecords[0] != null && metastoreRecords[0].length > 0) {
            if (getTitle() != null)
                success.println(ReportingConf.substituteVariables(getTitle()));
            if (getHeader() != null)
                success.println(this.getHeader());
            if (getNote() != null)
                success.println(this.getNote());

            if (getMetastoreQueryDefinition().getResultMessageHeader() != null) {
                success.println(getMetastoreQueryDefinition().getResultMessageHeader());
            }
            if (getMetastoreQueryDefinition().getResultMessageDetailHeader() != null) {
                success.println(getMetastoreQueryDefinition().getResultMessageDetailHeader());
            }
            for (int i = 0; i < metastoreRecords[0].length; i++) {
//                incSuccess(1);
                String[] record = new String[getMetastoreQueryDefinition().getListingColumns().length];
                for (int j = 0; j< getMetastoreQueryDefinition().getListingColumns().length; j++) {
                    record[j] = metastoreRecords[j][i];
                }
                String message = String.format(getMetastoreQueryDefinition().getResultMessageDetailTemplate(), record);
                success.println(message);
            }
//            incSuccess(1);
//            incProcessed(1);
        } else {
            success.println(getMetastoreQueryDefinition().getResultMessageHeader());
            success.println("\n > **Results empty**\n");
        }
//        setStatus(COMPLETED);
        setActive(false);
    }

    public MetastoreQuery getMetastoreQueryDefinition() {
        return metastoreQueryDefinition;
    }

    public void setMetastoreQueryDefinition(MetastoreQuery metastoreQueryDefinition) {
        this.metastoreQueryDefinition = metastoreQueryDefinition;
    }

    @Override
    public String toString() {
        return "MetastoreQueryProcess{}";
    }
}
