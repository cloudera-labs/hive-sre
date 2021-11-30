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
