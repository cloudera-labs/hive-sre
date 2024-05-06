# Hive Table Location - Unique Base Location w/ Counts (v.2.4.0.21.0-SNAPSHOT)

This report will show the unique base directories for table type in a database with an instance count for each.
 This should give you a good idea on the scope of the deviation.  If there is only a single base directory for a table in a database (regardless of count), then the table all share a common base directory.

| DB | Table Type | Base Directory | Instance Count |
|:---|:---|:---|:---| 
| default | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse | 3 |
| hdp50_workload | EXTERNAL_TABLE | hdfs://HDP50/apps/hive/warehouse/hdp50_workload.db | 3 |
| merge_files_migrate | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse/merge_files_migrate.db | 1 |
| lower_test | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse/lower_test.db | 2 |
| odd_jobs | EXTERNAL_TABLE | hdfs://HDP50/apps/hive/warehouse/odd_jobs.db | 1 |
| part_check | EXTERNAL_TABLE | hdfs://HDP50/apps/hive/warehouse/part_check.db | 1 |
| part_check | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse/part_check.db | 1 |
| test_avro | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse/test_avro.db | 1 |
| test_db | EXTERNAL_TABLE | hdfs://HDP50/apps/hive/warehouse/test_db.db | 1 |
| test_db | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse/test_db.db | 2 |
| covid_ga | EXTERNAL_TABLE | hdfs://HDP50/warehouse/tablespace/external/hive/covid_ga.db | 3 |
| tpcds_bin_partitioned_orc_10 | EXTERNAL_TABLE | hdfs://HDP50/apps/hive/warehouse/tpcds_bin_partitioned_orc_10.db | 1 |
| tpcds_bin_partitioned_orc_10 | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse/tpcds_bin_partitioned_orc_10.db | 26 |
| tpcds_bin_partitioned_orc_10 | VIRTUAL_VIEW |   | 1 |
| tpcds_text_10 | EXTERNAL_TABLE | hdfs://HDP50/tmp/tpcds-generate/10 | 24 |
| bucket_test | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse/bucket_test.db | 1 |
| z_hms_mirror_testdb_20220720_195237 | EXTERNAL_TABLE | hdfs://HDP50/apps/hive/warehouse/z_hms_mirror_testdb_20220720_195237.db | 3 |
| z_hms_mirror_testdb_20220720_195237 | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse/export_z_hms_mirror_testdb_20220720_195237 | 3 |
| z_hms_mirror_testdb_20220720_195237 | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse/z_hms_mirror_testdb_20220720_195237.db | 3 |
| wellcare | MANAGED_TABLE | hdfs://HDP50/apps/hive/warehouse/wellcare.db | 9 |
| wellcare | MANAGED_TABLE | hdfs://HOME90/apps/hive/warehouse/wellcare.db | 2 |
