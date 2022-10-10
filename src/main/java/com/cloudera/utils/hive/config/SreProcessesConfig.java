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
import com.cloudera.utils.sql.QueryDefinition;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private final Messages errors = new Messages(100);
    @JsonIgnore
    private final Messages warnings = new Messages(100);

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
}
