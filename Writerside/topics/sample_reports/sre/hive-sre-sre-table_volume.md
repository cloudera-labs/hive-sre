# Table / Partition Volume
| Database | Table | Type | Partition | Path | Dir. Count | File Count | Total Size | 
|:---|:---|:---|:---|:---|---:|---:|---:|
| my_test | test1 | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/my_test.db/test1 | 1 | 0 | 0 |
| default | test | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/test | 1 | 0 | 0 |
| credit_card | cc_balance | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/credit_card.db/cc_balance | 5 | 8 | 3.7 K |
| my_test | test2 | EXTERNAL_TABLE |   | hdfs://HOME90/data/ext/my_test.db/test2 | 1 | 0 | 0 |
| default | test1 | MANAGED_TABLE |   | hdfs://HOME90/apps/spark/warehouse/test1 | 1 | 1 | 2 |
| credit_card | cc_trans | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/credit_card.db/cc_trans | 1 | 0 | 0 |
| default | test_ext | EXTERNAL_TABLE |   | hdfs://HOME90/user/dstreev/test_ext | 1 | 3 | 1.0 K |
| credit_card | cc_trans_stream | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/credit_card.db/cc_trans_stream | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=5 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=5 | 1 | 1 | 5.3 M |
| default | test_ext2 | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/test_ext2 | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=8 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=8 | 1 | 1 | 5.3 M |
| priv_jonsnow | my_ext_test_01 | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/priv_jonsnow.db/my_ext_test_01 | 1 | 0 | 0 |
| priv_winterfell | test_01 | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/priv_winterfell.db/test_01 | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=6 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=6 | 1 | 1 | 5.3 M |
| priv_jonsnow | my_table | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/priv_jonsnow.db/my_table | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=2 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=2 | 1 | 1 | 5.3 M |
| priv_winterfell | test_managed | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/priv_winterfell.db/test_managed | 1 | 0 | 0 |
| priv_jonsnow | my_test_01 | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/priv_jonsnow.db/my_test_01 | 2 | 2 | 651 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=7 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=7 | 1 | 1 | 5.3 M |
| priv_jonsnow | my_test_02 | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/priv_jonsnow.db/my_test_02 | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=15 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=15 | 1 | 1 | 5.3 M |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=19 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=19 | 1 | 1 | 5.3 M |
| stuff2 | my_ext_test | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/stuff2.db/my_ext_test | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=9 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=9 | 1 | 1 | 5.3 M |
| stuff2 | my_managed_test | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/stuff2.db/my_managed_test | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=4 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=4 | 1 | 1 | 5.3 M |
| stuff2 | test_01 | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/stuff2.db/test_01 | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=13 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=13 | 1 | 1 | 5.3 M |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=11 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=11 | 1 | 1 | 5.3 M |
| stuff2 | test_02 | EXTERNAL_TABLE |   | hdfs://HOME90/user/dstreev/stuff2.db/test_02 | 1 | 0 | 0 |
| streaming_cc | cc_acct | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_acct | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=18 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=18 | 1 | 1 | 5.3 M |
| streaming_cc | cc_acct_daily | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_acct_daily | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=14 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=14 | 1 | 1 | 5.3 M |
| sys | bucketing_cols | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/bucketing_cols | 1 | 0 | 0 |
| streaming_cc | cc_acct_delta | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_acct_delta | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=17 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=17 | 1 | 1 | 5.3 M |
| sys | cds | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/cds | 1 | 0 | 0 |
| streaming_cc | cc_mrch_daily | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_mrch_daily | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=3 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=3 | 1 | 1 | 5.3 M |
| sys | columns_v2 | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/columns_v2 | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=10 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=10 | 1 | 1 | 5.3 M |
| streaming_cc | cc_trans_alt_from_streaming | MANAGED_TABLE | processing_cycle=2019-02-14 | hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming/processing_cycle=2019-02-14 | 4 | 7 | 44.9 G |
| sys | database_params | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/database_params | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=1 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=1 | 1 | 1 | 5.3 M |
| streaming_cc | cc_trans_alt_from_streaming | MANAGED_TABLE | processing_cycle=2019-02-15 | hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming/processing_cycle=2019-02-15 | 4 | 7 | 29.7 G |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=16 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=16 | 1 | 1 | 5.3 M |
| sys | db_privs | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/db_privs | 1 | 0 | 0 |
| streaming_cc | cc_trans_alt_from_streaming_2 | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming_2 | 1 | 0 | 0 |
| priv_dstreev | cc_trans_part | EXTERNAL_TABLE | section=12 | hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=12 | 1 | 1 | 5.3 M |
| sys | db_version | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/db_version | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-12 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-12 | 1 | 0 | 0 |
| priv_dstreev | junk1 | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/junk1 | 1 | 0 | 0 |
| sys | dbs | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/dbs | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-13 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-13 | 1 | 0 | 0 |
| sys | funcs | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/funcs | 1 | 0 | 0 |
| priv_dstreev | my_managed_table | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_managed_table | 2 | 2 | 870 |
| sys | global_privs | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/global_privs | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-14 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-14 | 1 | 0 | 0 |
| priv_dstreev | my_spark_managed | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/my_spark_managed | 1 | 1 | 6 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-15 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-15 | 1 | 0 | 0 |
| sys | key_constraints | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/key_constraints | 1 | 0 | 0 |
| priv_dstreev | my_test | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test | 3 | 3 | 790 |
| sys | mv_creation_metadata | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/mv_creation_metadata | 1 | 0 | 0 |
| priv_dstreev | my_test_01 | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test_01 | 3 | 4 | 1.5 K |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-16 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-16 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-17 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-17 | 1 | 0 | 0 |
| sys | mv_tables_used | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/mv_tables_used | 1 | 0 | 0 |
| priv_dstreev | my_test_convert | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test_convert | 1 | 0 | 0 |
| sys | part_col_privs | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/part_col_privs | 1 | 0 | 0 |
| priv_dstreev | my_test_ext | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test_ext | 3 | 3 | 790 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-18 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-18 | 1 | 0 | 0 |
| sys | part_col_stats | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/part_col_stats | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-19 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-19 | 1 | 0 | 0 |
| priv_dstreev | my_test_extr | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/my_test_extr | 1 | 0 | 0 |
| sys | part_privs | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/part_privs | 1 | 0 | 0 |
| priv_dstreev | spark_test_01 | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/spark_test_01 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-20 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-20 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-21 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-21 | 1 | 0 | 0 |
| sys | partition_key_vals | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/partition_key_vals | 1 | 0 | 0 |
| priv_dstreev | spark_test_02 | EXTERNAL_TABLE |   | hdfs://HOME90/user/dstreev/dataset/spark_test_02 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-22 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-22 | 1 | 0 | 0 |
| sys | partition_keys | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/partition_keys | 1 | 0 | 0 |
| priv_dstreev | test | MANAGED_TABLE |   | hdfs://HOME90/user/dstreev/datasets/internal.db/test | 1 | 0 | 0 |
| priv_dstreev | test_array | MANAGED_TABLE |   | hdfs://HOME90/user/dstreev/datasets/internal.db/test_array | 3 | 4 | 1.5 K |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-23 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-23 | 1 | 0 | 0 |
| sys | partition_params | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/partition_params | 1 | 0 | 0 |
| priv_dstreev | test_ext2 | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/test_ext2 | 1 | 0 | 0 |
| sys | partitions | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/partitions | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-24 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-24 | 1 | 0 | 0 |
| test_alt_managed | test_01 | MANAGED_TABLE |   | hdfs://HOME90/user/hive/alt_managed/test_01 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-25 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-25 | 1 | 0 | 0 |
| sys | role_map | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/role_map | 1 | 0 | 0 |
| sys | roles | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/roles | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-26 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-26 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-27 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-27 | 1 | 0 | 0 |
| sys | sd_params | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/sd_params | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-28 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-28 | 1 | 0 | 0 |
| sys | sds | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/sds | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-29 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-29 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-30 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-30 | 1 | 0 | 0 |
| sys | sequence_table | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/sequence_table | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-31 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-31 | 1 | 0 | 0 |
| sys | serde_params | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/serde_params | 1 | 0 | 0 |
| sys | serdes | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/serdes | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-32 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-32 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-33 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-33 | 1 | 0 | 0 |
| sys | skewed_col_names | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/skewed_col_names | 1 | 0 | 0 |
| sys | skewed_col_value_loc_map | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/skewed_col_value_loc_map | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-34 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-34 | 1 | 0 | 0 |
| sys | skewed_string_list | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/skewed_string_list | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-35 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-35 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-36 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-36 | 1 | 0 | 0 |
| sys | skewed_string_list_values | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/skewed_string_list_values | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-37 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-37 | 1 | 0 | 0 |
| sys | skewed_values | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/skewed_values | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-38 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-38 | 1 | 0 | 0 |
| sys | sort_cols | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/sort_cols | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-39 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-39 | 1 | 0 | 0 |
| sys | tab_col_stats | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/tab_col_stats | 1 | 0 | 0 |
| sys | table_params | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/table_params | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-40 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-40 | 1 | 0 | 0 |
| sys | tbl_col_privs | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/tbl_col_privs | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-41 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-41 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-42 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-42 | 1 | 0 | 0 |
| sys | tbl_privs | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/tbl_privs | 1 | 0 | 0 |
| sys | tbls | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/tbls | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-43 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-43 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-44 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-44 | 1 | 0 | 0 |
| sys | wm_mappings | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/wm_mappings | 1 | 0 | 0 |
| sys | wm_pools | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/wm_pools | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-45 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-45 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-46 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-46 | 1 | 0 | 0 |
| sys | wm_pools_to_triggers | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/wm_pools_to_triggers | 1 | 0 | 0 |
| sys | wm_resourceplans | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/wm_resourceplans | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-47 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-47 | 1 | 0 | 0 |
| sys | wm_triggers | EXTERNAL_TABLE |   | hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/wm_triggers | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-48 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-48 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-49 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-49 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-50 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-50 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-51 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-51 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-52 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-52 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-53 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-53 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-54 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-54 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-55 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-55 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-56 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-56 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-57 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-57 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-58 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-58 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=13-59 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-59 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-00 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-00 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-01 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-01 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-02 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-02 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-03 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-03 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-04 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-04 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-05 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-05 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-06 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-06 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-07 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-07 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-08 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-08 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-09 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-09 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | call_center | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/call_center | 2 | 2 | 5.1 K |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-10 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-10 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_page | MANAGED_TABLE |   | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_page | 2 | 2 | 415.7 K |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-11 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-11 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2452185 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2452185 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-12 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-12 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451051 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451051 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-13 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-13 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451804 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451804 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-14 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-14 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451592 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451592 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-15 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-15 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451225 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451225 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-16 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-16 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2452595 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2452595 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-17 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-17 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2452054 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2452054 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-18 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-18 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2452290 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2452290 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-19 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-19 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451461 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451461 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-20 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-20 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451935 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451935 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-21 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-21 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451897 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451897 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-22 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-22 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2452633 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2452633 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-23 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-23 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2452528 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2452528 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-24 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-24 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451356 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451356 | 2 | 1 | 1 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2452912 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2452912 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-25 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-25 | 1 | 0 | 0 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-26 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-26 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451187 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451187 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-27 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-27 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451630 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451630 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-28 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-28 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2450899 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2450899 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-29 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-29 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451077 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451077 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-30 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-30 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2451740 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2451740 | 2 | 1 | 1 |
| streaming_cc | cc_trans_bridge | EXTERNAL_TABLE | processing_cycle=14-31 | hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-31 | 1 | 0 | 0 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2450911 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2450911 | 2 | 1 | 1 |
| tpcds_bin_partitioned_orc_25 | catalog_returns | MANAGED_TABLE | cr_returned_date_sk=2452764 | hdfs://HOME90/warehouse/tablespace/managed/hive/tpcds_bin_partitioned_orc_25.db/catalog_returns/cr_returned_date_sk=2452764 | 2 | 1 | 1 |

> truncated for brevity