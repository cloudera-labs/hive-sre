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

package com.cloudera.utils.hive.sre;

import com.cloudera.utils.hive.config.HiveStrictManagedMigrationElements;
import com.cloudera.utils.sql.Parameter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class MetastoreQuery {

    private String query;
    @JsonProperty("parameters")
    private Map<String, Parameter> parameters;
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

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
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
