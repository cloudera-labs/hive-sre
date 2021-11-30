package com.cloudera.utils.sql;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Parameter {
//    @JsonProperty("default")
    private String initial;
    private String override;
    private Integer sqlType;
    private Integer location = 1;

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getOverride() {
        return override;
    }

    public void setOverride(String override) {
        this.override = override;
    }

    public Integer getSqlType() {
        return sqlType;
    }

    public void setSqlType(Integer sqlType) {
        this.sqlType = sqlType;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }
}
