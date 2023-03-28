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

package com.cloudera.utils.hive.dba;

import com.cloudera.utils.hive.config.DBStore;

public class QueryStore extends DBStore {
    public enum ANALYSIS_SOURCE { DAS, QP };

    private ANALYSIS_SOURCE source = ANALYSIS_SOURCE.QP;

    public ANALYSIS_SOURCE getSource() {
        return source;
    }

    public void setSource(ANALYSIS_SOURCE source) {
        this.source = source;
    }
}
