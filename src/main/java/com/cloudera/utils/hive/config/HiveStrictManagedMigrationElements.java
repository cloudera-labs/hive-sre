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
