# Procedures

## 1. Legacy Managed Non-Transactional (non-acid) Conversions to EXTERNAL/PURGE
Will execute changes against the Metastore DBMS directly for items found in the `u3` [process with ID 8](hive-sre-u3-procs.md).

## 2. ACID Table bucketing version update
Will execute changes against the Metastore DBMS directly for items found in the `u3` [process with ID 8](hive-sre-u3-procs.md).

## 3. Legacy Contrib Serde2 Replacement
Convert Legacy Serdes for:
- `org.apache.hadoop.hive.contrib.serde2.RegexSerDe`
- `org.apache.hadoop.hive.contrib.serde2.TypedBytesSerDe`
- `org.apache.hadoop.hive.contrib.serde2.MultiDelimitSerDe`

Other Serdes identified in the `u3` process will need to be independently addressed.
