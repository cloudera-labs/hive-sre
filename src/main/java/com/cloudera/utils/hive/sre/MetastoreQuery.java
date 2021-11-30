package com.cloudera.utils.hive.sre;

import com.cloudera.utils.hive.config.HiveStrictManagedMigrationElements;

public class MetastoreQuery {

    private String query;
    private String[] listingColumns;
    private String resultMessageHeader;
    private String resultMessageDetailHeader;
    private String resultMessageDetailTemplate;
    private CheckCalculation check;
    // HiveStrictManagedMigration Output Config
    private HiveStrictManagedMigrationElements hsmmElements;


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getListingColumns() {
        return listingColumns;
    }

    public void setListingColumns(String[] listingColumns) {
        this.listingColumns = listingColumns;
    }

    public String getResultMessageHeader() {
        return resultMessageHeader;
    }

    public void setResultMessageHeader(String resultMessageHeader) {
        this.resultMessageHeader = resultMessageHeader;
    }

    public String getResultMessageDetailHeader() {
        return resultMessageDetailHeader;
    }

    public CheckCalculation getCheck() {
        return check;
    }

    public void setCheck(CheckCalculation check) {
        this.check = check;
    }

    public void setResultMessageDetailHeader(String resultMessageDetailHeader) {
        this.resultMessageDetailHeader = resultMessageDetailHeader;
    }

    public String getResultMessageDetailTemplate() {
        return resultMessageDetailTemplate;
    }

    public void setResultMessageDetailTemplate(String resultMessageDetailTemplate) {
        this.resultMessageDetailTemplate = resultMessageDetailTemplate;
    }

    public HiveStrictManagedMigrationElements getHsmmElements() {
        return hsmmElements;
    }

    public void setHsmmElements(HiveStrictManagedMigrationElements hsmmElements) {
        this.hsmmElements = hsmmElements;
    }
}
