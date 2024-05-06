# Default Config Template

```
# Required to connect to Metastore RDBMS.  RDBMS driver needs to be included in the classpath
metastore_direct:
  uri: "FULL_RDMBS_URL_FOR_METASTORE_INCLUDING_THE_DB_NAME"
  type: MYSQL | POSTGRES | ORACLE
  # Needed for Oracle Connections to pick the right schema for hive.
  # initSql: "ALTER SESSION SET CURRENT_SCHEMA=<hive_schema>"
  connectionProperties:
    user: "DB_USER"
    password: "DB_PASSWORD"
  connectionPool:
    min: 3
    max: 5
# Control the number of threads to run scans with.  Should not exceed host core count.
# Increase parallelism will increase HDFS namenode pressure.  Advise monitoring namenode
# RPC latency while running this process.
parallelism: 4
queries:
  db_tbl_count:
    parameters:
      dbs:
        override: "%"
```
