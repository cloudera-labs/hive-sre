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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HiveStrictManagedMigrationIncludeListConfig {

    private static HiveStrictManagedMigrationIncludeListConfig single_instance = null;

    private HiveStrictManagedMigrationIncludeListConfig() {

    }

    public static HiveStrictManagedMigrationIncludeListConfig getInstance()
    {
        if (single_instance == null)
            single_instance = new HiveStrictManagedMigrationIncludeListConfig();
        return single_instance;
    }

    private Map<String, List<String>> databaseIncludeLists = new TreeMap<String, List<String>>();

    public Map<String, List<String>> getDatabaseIncludeLists() {
        return databaseIncludeLists;
    }

    public void setDatabaseIncludeLists(Map<String, List<String>> databaseIncludeLists) {
        this.databaseIncludeLists = databaseIncludeLists;
    }

    public void addTable(String database, String table) {
        List<String> tables = databaseIncludeLists.get(database);
        if (tables == null) {
            tables = new ArrayList<String>();
            databaseIncludeLists.put(database, tables);
        }
        tables.add(table);
    }
}
