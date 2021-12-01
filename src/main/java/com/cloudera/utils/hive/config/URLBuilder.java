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

public class URLBuilder {

    public enum TransportMode {
        HTTP, BINARY;
    }

    private String knoxProxy = null;
    private Boolean kerberized = Boolean.FALSE;
    private Boolean ssl = Boolean.FALSE;
    private String trustStore = null;
    private String trustStorePassword = null;
    private String user = null;
    private String password = null;
    private TransportMode mode = TransportMode.BINARY;
    private Boolean legacyManaged = Boolean.FALSE;
    private String hivePrincipal = null;

}
