# Hive Metastore Details
## Database Parameters

> **Results empty**

## Partition Count by Table Type
| Table Type | Count |
|:---|:---|
|EXTERNAL_TABLE|3464|
|MANAGED_TABLE|29669|
## Partition Count by Table
| Database | Table | Type | Num of Partitions|
|:---|:---|:---|:---|
|tpcds_bin_partitioned_orc_300|web_returns|MANAGED_TABLE|8736|
|tpcds_bin_partitioned_orc_300|catalog_returns|MANAGED_TABLE|8412|
|tpcds_bin_partitioned_orc_25|catalog_returns|MANAGED_TABLE|8384|
|tpcds_bin_partitioned_orc_300|store_returns|MANAGED_TABLE|8016|
|tpcds_bin_partitioned_orc_25|catalog_sales|MANAGED_TABLE|7352|
|tpcds_bin_partitioned_orc_300|catalog_sales|MANAGED_TABLE|7348|
|tpcds_bin_partitioned_orc_300|store_sales|MANAGED_TABLE|7296|
|tpcds_bin_partitioned_orc_300|web_sales|MANAGED_TABLE|7296|
|tpcds_bin_partitioned_orc_25|web_sales|MANAGED_TABLE|7296|
|tpcds_bin_partitioned_orc_25|store_sales|MANAGED_TABLE|7296|
|tpcds_bin_partitioned_orc_250|web_returns|MANAGED_TABLE|6552|
|tpcds_bin_partitioned_orc_250|catalog_returns|MANAGED_TABLE|6309|
|tpcds_bin_partitioned_orc_250|store_returns|EXTERNAL_TABLE|6012|
|tpcds_bin_partitioned_orc_250|catalog_sales|MANAGED_TABLE|5511|
|tpcds_bin_partitioned_orc_250|web_sales|MANAGED_TABLE|5472|
|streaming_cc|cc_trans_bridge|EXTERNAL_TABLE|4320|
|tpcds_bin_partitioned_orc_250|store_sales|MANAGED_TABLE|3648|
|tpcds_bin_partitioned_orc_300|inventory|MANAGED_TABLE|1044|
|tpcds_bin_partitioned_orc_250|inventory|MANAGED_TABLE|783|
|streaming_cc|cc_trans_from_incremental_append|MANAGED_TABLE|60|
|priv_dstreev|cc_trans_part|EXTERNAL_TABLE|57|
|streaming_cc|cc_trans_alt_from_streaming|MANAGED_TABLE|8|
|streaming_cc|cc_trans_alt_from_streaming_ext|EXTERNAL_TABLE|3|
## Table Parameter Summary
| Parameter | Count |
|:---|:---|
|COLUMN_STATS_ACCURATE|107|
|EXTERNAL|192|
|TRANSLATED_TO_EXTERNAL|3|
|bucketing_version|252|
|external.table.purge|4|
|hive.sql.database.type|44|
|hive.sql.query|44|
|last_modified_by|3|
|last_modified_time|3|
|numFiles|250|
|numFilesErasureCoded|83|
|numRows|124|
|rawDataSize|124|
|spark.sql.create.version|7|
|spark.sql.sources.provider|3|
|spark.sql.sources.schema.numParts|7|
|spark.sql.sources.schema.part.0|7|
|storage_handler|44|
|totalSize|250|
|transactional|99|
|transactional_properties|81|
|transient_lastDdlTime|300|
## Table Parameter Use Summary
| Table Type | Parameter | Count |
|:---|:---|:---|
|MANAGED_TABLE|true|97|
|EXTERNAL_TABLE|true|2|
## Transactional Table Parameter Summary
| Database | Table Type | Transaction Flag | Count|
|:---|:---|:---|:---|
|credit_card|MANAGED_TABLE|true|3|
|default|MANAGED_TABLE|true|1|
|priv_dstreev|EXTERNAL_TABLE|true|2|
|priv_dstreev|MANAGED_TABLE|true|6|
|priv_jonsnow|MANAGED_TABLE|true|2|
|priv_winterfell|MANAGED_TABLE|true|1|
|streaming_cc|MANAGED_TABLE|true|10|
|stuff2|MANAGED_TABLE|true|2|
|test_alt_managed|MANAGED_TABLE|true|1|
|tpcds_bin_partitioned_orc_25|MANAGED_TABLE|true|24|
|tpcds_bin_partitioned_orc_250|MANAGED_TABLE|true|23|
|tpcds_bin_partitioned_orc_300|MANAGED_TABLE|true|24|
## SERDE Table Type Use Summary
| Table Type | Input Format | Output Format | Count |
|:---|:---|:---|:---|
|EXTERNAL_TABLE|null|null|44|
|EXTERNAL_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|3|
|EXTERNAL_TABLE|org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat|org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat|2|
|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|142|
|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|97|
|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat|org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat|1|
|VIRTUAL_VIEW|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|11|
## Serde by Database / Table Type Summary
| Database | Table Type | Input Format | Output Format | Count |
|:---|:---|:---|:---|:---|
|credit_card|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|1|
|credit_card|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|3|
|default|EXTERNAL_TABLE|org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat|org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat|2|
|default|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|1|
|default|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|1|
|default|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat|org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat|1|
|information_schema|VIRTUAL_VIEW|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|8|
|my_test|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|2|
|priv_dstreev|EXTERNAL_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|2|
|priv_dstreev|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|7|
|priv_dstreev|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|6|
|priv_jonsnow|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|2|
|priv_jonsnow|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|2|
|priv_winterfell|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|1|
|priv_winterfell|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|1|
|streaming_cc|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|4|
|streaming_cc|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|10|
|stuff2|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|2|
|stuff2|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|2|
|sys|EXTERNAL_TABLE|null|null|44|
|sys|VIRTUAL_VIEW|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|3|
|test_alt_managed|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|1|
|tpcds_bin_partitioned_orc_25|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|24|
|tpcds_bin_partitioned_orc_250|EXTERNAL_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|1|
|tpcds_bin_partitioned_orc_250|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|23|
|tpcds_bin_partitioned_orc_300|MANAGED_TABLE|org.apache.hadoop.hive.ql.io.orc.OrcInputFormat|org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat|24|
|tpcds_text_25|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|24|
|tpcds_text_250|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|26|
|tpcds_text_26|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|24|
|tpcds_text_3|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|24|
|tpcds_text_300|EXTERNAL_TABLE|org.apache.hadoop.mapred.TextInputFormat|org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat|24|
