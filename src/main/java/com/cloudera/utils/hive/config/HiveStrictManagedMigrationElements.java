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

package com.cloudera.utils.hive.config;

public class HiveStrictManagedMigrationElements {
    private String databaseField;
    private String tableField;

    public String getDatabaseField() {
        return databaseField;
    }

    public void setDatabaseField(String databaseField) {
        this.databaseField = databaseField;
    }

    public String getTableField() {
        return tableField;
    }

    public void setTableField(String tableField) {
        this.tableField = tableField;
    }
}
