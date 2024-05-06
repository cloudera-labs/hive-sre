# Default Config Template

```yaml
# Copyright 2021 Cloudera, Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

## Require for `u3` and `sre`
metastore_direct:
  uri: "FULL_JDBC_DB_URL"
  type: MYSQL | POSTGRES | ORACLE
  # Required for Oracle Connections to Select proper schema
  #initSql: "ALTER SESSION SET CURRENT_SCHEMA=<hive_schema>"
  # Using apache.commons dbcp2 see: https://commons.apache.org/proper/commons-dbcp/configuration.html
  connectionProperties:
    user: "DB_USER"
    password: "DB_PASSWORD"
  connectionPool:
    min: 3
    max: 5
## NOT required for `u3` and `sre`
query_analysis:
  source: DAS | QP
  uri: "<FULL_JDBC_DB_URL>"
  # Currently only POSTGRES is supported for either DAS|QP
  type: POSTGRES
  connectionProperties:
    user: "DB_USER"
    password: "DB_PASSWORD"
  connectionPool:
    min: 3
    max: 10
parallelism: 4
queries:
  db_tbl_count:
    parameters:
      dbs:
        override: "%"
```