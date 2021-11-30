package com.cloudera.utils.hive.config;

import com.cloudera.utils.sql.QueryDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/*
The control structure used to connect to resources used by the @link ProcessContainer

 */
public class SreProcessesConfig {
    @JsonProperty(value = "metastore_direct")
    private Metastore metastoreDirect;
    private Metastore hs2;
    private int parallelism = 2;
    private int reportingInterval = 500;

    @JsonProperty("queries")
    private Map<String, QueryDefinition> queries = new LinkedHashMap<String, QueryDefinition>();

    public QueryDefinition getQuery(String name) {
        return queries.get(name);
    }

    public Metastore getMetastoreDirect() {
        return metastoreDirect;
    }

    public void setMetastoreDirect(Metastore metastoreDirect) {
        this.metastoreDirect = metastoreDirect;
    }

    public Metastore getHs2() {
        return hs2;
    }

    public void setHs2(Metastore hs2) {
        this.hs2 = hs2;
    }

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    public int getReportingInterval() {
        return reportingInterval;
    }

    public void setReportingInterval(int reportingInterval) {
        this.reportingInterval = reportingInterval;
    }

    public void validate() {
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        Set<ConstraintViolation<Metastore>> violations = validator.validate(metastoreDirect);
    }
}
