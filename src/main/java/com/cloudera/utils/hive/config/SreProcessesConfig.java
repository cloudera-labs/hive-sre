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

import com.cloudera.utils.Messages;
import com.cloudera.utils.hive.dba.QueryStore;
import com.cloudera.utils.sql.QueryDefinition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/*
The control structure used to connect to resources used by the @link ProcessContainer

 */
public class SreProcessesConfig {
    @JsonProperty(value = "metastore_direct")
    private DBStore metastoreDirect;
    private DBStore hs2;
    @JsonProperty(value = "query_analysis")
    private QueryStore queryAnalysis;

    private int parallelism = 2;
    private int reportingInterval = 500;

    @JsonProperty("queries")
    private Map<String, QueryDefinition> queries = new LinkedHashMap<String, QueryDefinition>();

    @JsonIgnore
    private final Messages errors = new Messages(100);
    @JsonIgnore
    private final Messages warnings = new Messages(100);

    public QueryDefinition getQuery(String name) {
        return queries.get(name);
    }

    public DBStore getMetastoreDirect() {
        return metastoreDirect;
    }

    public void setMetastoreDirect(DBStore metastoreDirect) {
        this.metastoreDirect = metastoreDirect;
    }

    public QueryStore getQueryAnalysis() {
        return queryAnalysis;
    }

    public void setQueryAnalysis(QueryStore queryAnalysis) {
        this.queryAnalysis = queryAnalysis;
    }

    public DBStore getHs2() {
        return hs2;
    }

    public void setHs2(DBStore hs2) {
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

    public Messages getErrors() {
        return errors;
    }

    public Boolean hasErrors() {
        if (errors.getReturnCode() > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Messages getWarnings() {
        return warnings;
    }

    public Boolean validate() {
        Boolean rtn = Boolean.TRUE;


        return rtn;
    }

    public static SreProcessesConfig init(String configFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SreProcessesConfig sreConfig = null;
        try {
            File cfgFile = new File(configFile);
            if (!cfgFile.exists()) {
                throw new RuntimeException("Missing configuration file: " + configFile);
            } else {
                System.out.println("Using Config: " + configFile);
            }
            String yamlCfgFile = FileUtils.readFileToString(cfgFile, Charset.forName("UTF-8"));
            sreConfig = mapper.readerFor(SreProcessesConfig.class).readValue(yamlCfgFile);
            sreConfig.validate();

        } catch (
                IOException e) {
            throw new RuntimeException("Issue getting configs", e);
        }
        return sreConfig;
    }
}
