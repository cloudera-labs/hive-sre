/*
 * Copyright 2021 Cloudera, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
