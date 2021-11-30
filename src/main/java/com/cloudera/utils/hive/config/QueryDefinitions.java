package com.cloudera.utils.hive.config;

import com.cloudera.utils.sql.QueryDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryDefinitions {

    @JsonProperty("query_definitions")
    private Map<String, QueryDefinition> queryDefinitions = new LinkedHashMap<String, QueryDefinition>();

    public QueryDefinition getQueryDefinition(String name) {
        return queryDefinitions.get(name);
    }

    //    private String selectDbs;
//
//    public String getSelectDbs() {
//        return selectDbs;
//    }
//
//    public void setSelectDbs(String selectDbs) {
//        this.selectDbs = selectDbs;
//    }
}
