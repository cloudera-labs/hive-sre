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

package com.cloudera.utils.hive.config;


import javax.validation.constraints.NotNull;
import java.util.Properties;

public class Metastore {
    public enum DB_TYPE { MYSQL, POSTGRES, ORACLE
//        , MSSQL
    };
    @NotNull(message = "Missing Metastore JDBC URI")
    private String uri;
    @NotNull(message = "Need to specify one of: MYSQL, ORACLE, POSTGRES, MSSQL")
    private DB_TYPE type = DB_TYPE.MYSQL; // Default
    // Run for each fetch connection.  Mainly used to help set the DB/Schema for Oracle
    private String initSql;
    @NotNull
    private Properties connectionProperties;
    @NotNull
    private ConnectionPool connectionPool;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public DB_TYPE getType() {
        return type;
    }

    public void setType(DB_TYPE type) {
        this.type = type;
    }

    public String getInitSql() {
        return initSql;
    }

    public void setInitSql(String initSql) {
        this.initSql = initSql;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
