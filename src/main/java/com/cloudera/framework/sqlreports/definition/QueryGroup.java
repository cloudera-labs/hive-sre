package com.cloudera.framework.sqlreports.definition;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QueryGroup {

    @JsonProperty(value = "detail_references")
    private List<String> detailReferences = null;

    public List<String> getDetailReferences() {
        return detailReferences;
    }

    public void setDetailReferences(List<String> detailReferences) {
        this.detailReferences = detailReferences;
    }
}
