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
