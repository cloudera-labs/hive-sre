package com.cloudera.utils.sql;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class QueryDefinition {
    private String statement;
    @JsonProperty("parameters")
    private Map<String, Parameter> parameters;

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }
}
