# Missing Directory Remediation Options

| DB.Table:Part | Hive SQL (recommended) | HDFS |
|:---|:---|:---|
| my_test.test1 | DROP TABLE IF EXIST `my_test`.`test1`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/my_test.db/test1 |
| default.test | DROP TABLE IF EXIST `default`.`test`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/test |
| credit_card.cc_balance | DROP TABLE IF EXIST `credit_card`.`cc_balance`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/credit_card.db/cc_balance |
| credit_card.cc_balance | DROP TABLE IF EXIST `credit_card`.`cc_balance`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/credit_card.db/cc_balance |
| credit_card.cc_balance | DROP TABLE IF EXIST `credit_card`.`cc_balance`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/credit_card.db/cc_balance |
| credit_card.cc_balance | DROP TABLE IF EXIST `credit_card`.`cc_balance`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/credit_card.db/cc_balance |
| credit_card.cc_balance | DROP TABLE IF EXIST `credit_card`.`cc_balance`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/credit_card.db/cc_balance |
| my_test.test2 | DROP TABLE IF EXIST `my_test`.`test2`; | mkdir -p hdfs://HOME90/data/ext/my_test.db/test2 |
| default.test1 | DROP TABLE IF EXIST `default`.`test1`; | mkdir -p hdfs://HOME90/apps/spark/warehouse/test1 |
| default.test1 | DROP TABLE IF EXIST `default`.`test1`; | mkdir -p hdfs://HOME90/apps/spark/warehouse/test1 |
| credit_card.cc_trans | DROP TABLE IF EXIST `credit_card`.`cc_trans`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/credit_card.db/cc_trans |
| credit_card.cc_trans_min_ext_stream | DROP TABLE IF EXIST `credit_card`.`cc_trans_min_ext_stream`; | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_min |
| default.test_ext | DROP TABLE IF EXIST `default`.`test_ext`; | mkdir -p hdfs://HOME90/user/dstreev/test_ext |
| default.test_ext | DROP TABLE IF EXIST `default`.`test_ext`; | mkdir -p hdfs://HOME90/user/dstreev/test_ext |
| default.test_ext | DROP TABLE IF EXIST `default`.`test_ext`; | mkdir -p hdfs://HOME90/user/dstreev/test_ext |
| default.test_ext | DROP TABLE IF EXIST `default`.`test_ext`; | mkdir -p hdfs://HOME90/user/dstreev/test_ext |
| credit_card.cc_trans_stream | DROP TABLE IF EXIST `credit_card`.`cc_trans_stream`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/credit_card.db/cc_trans_stream |
| priv_dstreev.cc_trans | DROP TABLE IF EXIST `priv_dstreev`.`cc_trans`; | mkdir -p hdfs://HOME90/user/dstreev/datasets/internal.db/cc_trans |
| default.test_ext2 | DROP TABLE IF EXIST `default`.`test_ext2`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/test_ext2 |
| priv_jonsnow.my_ext_test_01 | DROP TABLE IF EXIST `priv_jonsnow`.`my_ext_test_01`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/priv_jonsnow.db/my_ext_test_01 |
| priv_winterfell.test_01 | DROP TABLE IF EXIST `priv_winterfell`.`test_01`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/priv_winterfell.db/test_01 |
| priv_winterfell.test_managed | DROP TABLE IF EXIST `priv_winterfell`.`test_managed`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_winterfell.db/test_managed |
| priv_jonsnow.my_table | DROP TABLE IF EXIST `priv_jonsnow`.`my_table`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/priv_jonsnow.db/my_table |
| priv_dstreev.cc_trans_part:section="5" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="5"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=5 |
| priv_dstreev.cc_trans_part:section="5" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="5"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=5 |
| priv_jonsnow.my_test_01 | DROP TABLE IF EXIST `priv_jonsnow`.`my_test_01`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_jonsnow.db/my_test_01 |
| priv_jonsnow.my_test_01 | DROP TABLE IF EXIST `priv_jonsnow`.`my_test_01`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_jonsnow.db/my_test_01 |
| priv_jonsnow.my_test_02 | DROP TABLE IF EXIST `priv_jonsnow`.`my_test_02`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_jonsnow.db/my_test_02 |
| priv_dstreev.cc_trans_part:section="8" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="8"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=8 |
| priv_dstreev.cc_trans_part:section="8" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="8"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=8 |
| stuff2.my_ext_test | DROP TABLE IF EXIST `stuff2`.`my_ext_test`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/stuff2.db/my_ext_test |
| priv_dstreev.cc_trans_part:section="6" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="6"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=6 |
| priv_dstreev.cc_trans_part:section="6" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="6"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=6 |
| stuff2.my_managed_test | DROP TABLE IF EXIST `stuff2`.`my_managed_test`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/stuff2.db/my_managed_test |
| stuff2.test_01 | DROP TABLE IF EXIST `stuff2`.`test_01`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/stuff2.db/test_01 |
| priv_dstreev.cc_trans_part:section="2" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="2"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=2 |
| priv_dstreev.cc_trans_part:section="2" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="2"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=2 |
| stuff2.test_02 | DROP TABLE IF EXIST `stuff2`.`test_02`; | mkdir -p hdfs://HOME90/user/dstreev/stuff2.db/test_02 |
| priv_dstreev.cc_trans_part:section="7" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="7"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=7 |
| priv_dstreev.cc_trans_part:section="7" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="7"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=7 |
| sys.bucketing_cols | DROP TABLE IF EXIST `sys`.`bucketing_cols`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/bucketing_cols |
| sys.cds | DROP TABLE IF EXIST `sys`.`cds`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/cds |
| priv_dstreev.cc_trans_part:section="15" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="15"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=15 |
| priv_dstreev.cc_trans_part:section="15" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="15"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=15 |
| streaming_cc.cc_acct | DROP TABLE IF EXIST `streaming_cc`.`cc_acct`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_acct |
| sys.columns_v2 | DROP TABLE IF EXIST `sys`.`columns_v2`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/columns_v2 |
| streaming_cc.cc_acct_daily | DROP TABLE IF EXIST `streaming_cc`.`cc_acct_daily`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_acct_daily |
| sys.database_params | DROP TABLE IF EXIST `sys`.`database_params`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/database_params |
| priv_dstreev.cc_trans_part:section="19" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="19"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=19 |
| priv_dstreev.cc_trans_part:section="19" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="19"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=19 |
| streaming_cc.cc_acct_delta | DROP TABLE IF EXIST `streaming_cc`.`cc_acct_delta`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_acct_delta |
| sys.db_privs | DROP TABLE IF EXIST `sys`.`db_privs`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/db_privs |
| streaming_cc.cc_mrch_daily | DROP TABLE IF EXIST `streaming_cc`.`cc_mrch_daily`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_mrch_daily |
| priv_dstreev.cc_trans_part:section="9" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="9"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=9 |
| priv_dstreev.cc_trans_part:section="9" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="9"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=9 |
| sys.db_version | DROP TABLE IF EXIST `sys`.`db_version`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/db_version |
| sys.dbs | DROP TABLE IF EXIST `sys`.`dbs`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/dbs |
| streaming_cc.cc_trans_alt_from_streaming:processing_cycle="2019-02-14" | ALTER TABLE `streaming_cc`.`cc_trans_alt_from_streaming` DROP IF EXISTS PARTITION (processing_cycle="2019-02-14"); | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming/processing_cycle=2019-02-14 |
| streaming_cc.cc_trans_alt_from_streaming:processing_cycle="2019-02-14" | ALTER TABLE `streaming_cc`.`cc_trans_alt_from_streaming` DROP IF EXISTS PARTITION (processing_cycle="2019-02-14"); | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming/processing_cycle=2019-02-14 |
| streaming_cc.cc_trans_alt_from_streaming:processing_cycle="2019-02-14" | ALTER TABLE `streaming_cc`.`cc_trans_alt_from_streaming` DROP IF EXISTS PARTITION (processing_cycle="2019-02-14"); | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming/processing_cycle=2019-02-14 |
| streaming_cc.cc_trans_alt_from_streaming:processing_cycle="2019-02-14" | ALTER TABLE `streaming_cc`.`cc_trans_alt_from_streaming` DROP IF EXISTS PARTITION (processing_cycle="2019-02-14"); | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming/processing_cycle=2019-02-14 |
| priv_dstreev.cc_trans_part:section="4" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="4"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=4 |
| priv_dstreev.cc_trans_part:section="4" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="4"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=4 |
| sys.funcs | DROP TABLE IF EXIST `sys`.`funcs`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/funcs |
| sys.global_privs | DROP TABLE IF EXIST `sys`.`global_privs`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/global_privs |
| streaming_cc.cc_trans_alt_from_streaming:processing_cycle="2019-02-15" | ALTER TABLE `streaming_cc`.`cc_trans_alt_from_streaming` DROP IF EXISTS PARTITION (processing_cycle="2019-02-15"); | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming/processing_cycle=2019-02-15 |
| streaming_cc.cc_trans_alt_from_streaming:processing_cycle="2019-02-15" | ALTER TABLE `streaming_cc`.`cc_trans_alt_from_streaming` DROP IF EXISTS PARTITION (processing_cycle="2019-02-15"); | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming/processing_cycle=2019-02-15 |
| streaming_cc.cc_trans_alt_from_streaming:processing_cycle="2019-02-15" | ALTER TABLE `streaming_cc`.`cc_trans_alt_from_streaming` DROP IF EXISTS PARTITION (processing_cycle="2019-02-15"); | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming/processing_cycle=2019-02-15 |
| streaming_cc.cc_trans_alt_from_streaming:processing_cycle="2019-02-15" | ALTER TABLE `streaming_cc`.`cc_trans_alt_from_streaming` DROP IF EXISTS PARTITION (processing_cycle="2019-02-15"); | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming/processing_cycle=2019-02-15 |
| priv_dstreev.cc_trans_part:section="13" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="13"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=13 |
| priv_dstreev.cc_trans_part:section="13" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="13"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=13 |
| sys.key_constraints | DROP TABLE IF EXIST `sys`.`key_constraints`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/key_constraints |
| streaming_cc.cc_trans_alt_from_streaming_2 | DROP TABLE IF EXIST `streaming_cc`.`cc_trans_alt_from_streaming_2`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/streaming_cc.db/cc_trans_alt_from_streaming_2 |
| sys.mv_creation_metadata | DROP TABLE IF EXIST `sys`.`mv_creation_metadata`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/mv_creation_metadata |
| streaming_cc.cc_trans_alt_from_streaming_ext:processing_cycle="2019-02-14" | ALTER TABLE `streaming_cc`.`cc_trans_alt_from_streaming_ext` DROP IF EXISTS PARTITION (processing_cycle="2019-02-14"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_alt/processing_cycle=2019-02-14 |
| priv_dstreev.cc_trans_part:section="11" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="11"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=11 |
| priv_dstreev.cc_trans_part:section="11" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="11"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=11 |
| sys.mv_tables_used | DROP TABLE IF EXIST `sys`.`mv_tables_used`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/mv_tables_used |
| streaming_cc.cc_trans_bridge:processing_cycle="13-12" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-12"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-12 |
| priv_dstreev.cc_trans_part:section="18" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="18"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=18 |
| priv_dstreev.cc_trans_part:section="18" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="18"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=18 |
| sys.part_col_privs | DROP TABLE IF EXIST `sys`.`part_col_privs`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/part_col_privs |
| sys.part_col_stats | DROP TABLE IF EXIST `sys`.`part_col_stats`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/part_col_stats |
| streaming_cc.cc_trans_bridge:processing_cycle="13-13" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-13"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-13 |
| priv_dstreev.cc_trans_part:section="14" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="14"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=14 |
| priv_dstreev.cc_trans_part:section="14" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="14"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=14 |
| sys.part_privs | DROP TABLE IF EXIST `sys`.`part_privs`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/part_privs |
| sys.partition_key_vals | DROP TABLE IF EXIST `sys`.`partition_key_vals`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/partition_key_vals |
| streaming_cc.cc_trans_bridge:processing_cycle="13-14" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-14"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-14 |
| priv_dstreev.cc_trans_part:section="17" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="17"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=17 |
| priv_dstreev.cc_trans_part:section="17" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="17"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=17 |
| sys.partition_keys | DROP TABLE IF EXIST `sys`.`partition_keys`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/partition_keys |
| streaming_cc.cc_trans_bridge:processing_cycle="13-15" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-15"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-15 |
| priv_dstreev.cc_trans_part:section="3" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="3"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=3 |
| priv_dstreev.cc_trans_part:section="3" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="3"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=3 |
| sys.partition_params | DROP TABLE IF EXIST `sys`.`partition_params`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/partition_params |
| sys.partitions | DROP TABLE IF EXIST `sys`.`partitions`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/partitions |
| streaming_cc.cc_trans_bridge:processing_cycle="13-16" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-16"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-16 |
| priv_dstreev.cc_trans_part:section="10" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="10"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=10 |
| priv_dstreev.cc_trans_part:section="10" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="10"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=10 |
| sys.role_map | DROP TABLE IF EXIST `sys`.`role_map`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/role_map |
| streaming_cc.cc_trans_bridge:processing_cycle="13-17" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-17"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-17 |
| priv_dstreev.cc_trans_part:section="1" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="1"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=1 |
| priv_dstreev.cc_trans_part:section="1" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="1"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=1 |
| sys.roles | DROP TABLE IF EXIST `sys`.`roles`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/roles |
| sys.scheduled_executions | DROP TABLE IF EXIST `sys`.`scheduled_executions`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/scheduled_executions |
| sys.scheduled_queries | DROP TABLE IF EXIST `sys`.`scheduled_queries`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/scheduled_queries |
| streaming_cc.cc_trans_bridge:processing_cycle="13-18" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-18"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-18 |
| priv_dstreev.cc_trans_part:section="16" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="16"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=16 |
| priv_dstreev.cc_trans_part:section="16" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="16"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=16 |
| sys.sd_params | DROP TABLE IF EXIST `sys`.`sd_params`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/sd_params |
| streaming_cc.cc_trans_bridge:processing_cycle="13-19" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-19"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-19 |
| priv_dstreev.cc_trans_part:section="12" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="12"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=12 |
| priv_dstreev.cc_trans_part:section="12" | ALTER TABLE `priv_dstreev`.`cc_trans_part` DROP IF EXISTS PARTITION (section="12"); | mkdir -p hdfs://HOME90/user/dstreev/datasets/external/cc_trans_part/section=12 |
| sys.sds | DROP TABLE IF EXIST `sys`.`sds`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/sds |
| priv_dstreev.junk1 | DROP TABLE IF EXIST `priv_dstreev`.`junk1`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/junk1 |
| sys.sequence_table | DROP TABLE IF EXIST `sys`.`sequence_table`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/sequence_table |
| streaming_cc.cc_trans_bridge:processing_cycle="13-20" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-20"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-20 |
| priv_dstreev.my_managed_table | DROP TABLE IF EXIST `priv_dstreev`.`my_managed_table`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_managed_table |
| priv_dstreev.my_managed_table | DROP TABLE IF EXIST `priv_dstreev`.`my_managed_table`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_managed_table |
| sys.serde_params | DROP TABLE IF EXIST `sys`.`serde_params`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/serde_params |
| sys.serdes | DROP TABLE IF EXIST `sys`.`serdes`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/serdes |
| priv_dstreev.my_spark_managed | DROP TABLE IF EXIST `priv_dstreev`.`my_spark_managed`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/my_spark_managed |
| priv_dstreev.my_spark_managed | DROP TABLE IF EXIST `priv_dstreev`.`my_spark_managed`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/my_spark_managed |
| streaming_cc.cc_trans_bridge:processing_cycle="13-21" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-21"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-21 |
| sys.skewed_col_names | DROP TABLE IF EXIST `sys`.`skewed_col_names`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/skewed_col_names |
| priv_dstreev.my_test | DROP TABLE IF EXIST `priv_dstreev`.`my_test`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test |
| priv_dstreev.my_test | DROP TABLE IF EXIST `priv_dstreev`.`my_test`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test |
| streaming_cc.cc_trans_bridge:processing_cycle="13-22" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-22"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-22 |
| sys.skewed_col_value_loc_map | DROP TABLE IF EXIST `sys`.`skewed_col_value_loc_map`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/skewed_col_value_loc_map |
| priv_dstreev.my_test_01 | DROP TABLE IF EXIST `priv_dstreev`.`my_test_01`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test_01 |
| priv_dstreev.my_test_01 | DROP TABLE IF EXIST `priv_dstreev`.`my_test_01`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test_01 |
| priv_dstreev.my_test_01 | DROP TABLE IF EXIST `priv_dstreev`.`my_test_01`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test_01 |
| priv_dstreev.my_test_convert | DROP TABLE IF EXIST `priv_dstreev`.`my_test_convert`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test_convert |
| sys.skewed_string_list | DROP TABLE IF EXIST `sys`.`skewed_string_list`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/skewed_string_list |
| streaming_cc.cc_trans_bridge:processing_cycle="13-23" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-23"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-23 |
| sys.skewed_string_list_values | DROP TABLE IF EXIST `sys`.`skewed_string_list_values`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/skewed_string_list_values |
| priv_dstreev.my_test_ext | DROP TABLE IF EXIST `priv_dstreev`.`my_test_ext`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test_ext |
| priv_dstreev.my_test_ext | DROP TABLE IF EXIST `priv_dstreev`.`my_test_ext`; | mkdir -p hdfs://HOME90/warehouse/tablespace/managed/hive/priv_dstreev.db/my_test_ext |
| streaming_cc.cc_trans_bridge:processing_cycle="13-24" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-24"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-24 |
| priv_dstreev.my_test_extr | DROP TABLE IF EXIST `priv_dstreev`.`my_test_extr`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/my_test_extr |
| sys.skewed_values | DROP TABLE IF EXIST `sys`.`skewed_values`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/skewed_values |
| sys.sort_cols | DROP TABLE IF EXIST `sys`.`sort_cols`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/sort_cols |
| priv_dstreev.spark_test_01 | DROP TABLE IF EXIST `priv_dstreev`.`spark_test_01`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/spark_test_01 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-25" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-25"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-25 |
| sys.tab_col_stats | DROP TABLE IF EXIST `sys`.`tab_col_stats`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/tab_col_stats |
| priv_dstreev.spark_test_02 | DROP TABLE IF EXIST `priv_dstreev`.`spark_test_02`; | mkdir -p hdfs://HOME90/user/dstreev/dataset/spark_test_02 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-26" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-26"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-26 |
| sys.table_params | DROP TABLE IF EXIST `sys`.`table_params`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/table_params |
| priv_dstreev.test | DROP TABLE IF EXIST `priv_dstreev`.`test`; | mkdir -p hdfs://HOME90/user/dstreev/datasets/internal.db/test |
| sys.tbl_col_privs | DROP TABLE IF EXIST `sys`.`tbl_col_privs`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/tbl_col_privs |
| priv_dstreev.test_array | DROP TABLE IF EXIST `priv_dstreev`.`test_array`; | mkdir -p hdfs://HOME90/user/dstreev/datasets/internal.db/test_array |
| priv_dstreev.test_array | DROP TABLE IF EXIST `priv_dstreev`.`test_array`; | mkdir -p hdfs://HOME90/user/dstreev/datasets/internal.db/test_array |
| priv_dstreev.test_array | DROP TABLE IF EXIST `priv_dstreev`.`test_array`; | mkdir -p hdfs://HOME90/user/dstreev/datasets/internal.db/test_array |
| streaming_cc.cc_trans_bridge:processing_cycle="13-27" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-27"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-27 |
| sys.tbl_privs | DROP TABLE IF EXIST `sys`.`tbl_privs`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/tbl_privs |
| priv_dstreev.test_ext2 | DROP TABLE IF EXIST `priv_dstreev`.`test_ext2`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/priv_dstreev.db/test_ext2 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-28" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-28"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-28 |
| sys.tbls | DROP TABLE IF EXIST `sys`.`tbls`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/tbls |
| test_alt_managed.test_01 | DROP TABLE IF EXIST `test_alt_managed`.`test_01`; | mkdir -p hdfs://HOME90/user/hive/alt_managed/test_01 |
| sys.wm_mappings | DROP TABLE IF EXIST `sys`.`wm_mappings`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/wm_mappings |
| streaming_cc.cc_trans_bridge:processing_cycle="13-29" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-29"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-29 |
| sys.wm_pools | DROP TABLE IF EXIST `sys`.`wm_pools`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/wm_pools |
| streaming_cc.cc_trans_bridge:processing_cycle="13-30" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-30"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-30 |
| sys.wm_pools_to_triggers | DROP TABLE IF EXIST `sys`.`wm_pools_to_triggers`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/wm_pools_to_triggers |
| sys.wm_resourceplans | DROP TABLE IF EXIST `sys`.`wm_resourceplans`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/wm_resourceplans |
| streaming_cc.cc_trans_bridge:processing_cycle="13-31" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-31"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-31 |
| sys.wm_triggers | DROP TABLE IF EXIST `sys`.`wm_triggers`; | mkdir -p hdfs://HOME90/warehouse/tablespace/external/hive/sys.db/wm_triggers |
| streaming_cc.cc_trans_bridge:processing_cycle="13-32" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-32"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-32 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-33" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-33"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-33 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-34" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-34"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-34 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-35" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-35"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-35 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-36" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-36"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-36 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-37" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-37"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-37 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-38" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-38"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-38 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-39" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-39"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-39 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-40" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-40"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-40 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-41" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-41"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-41 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-42" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-42"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-42 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-43" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-43"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-43 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-44" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-44"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-44 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-45" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-45"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-45 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-46" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-46"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-46 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-47" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-47"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-47 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-48" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-48"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-48 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-49" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-49"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-49 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-50" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-50"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-50 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-51" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-51"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-51 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-52" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-52"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-52 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-53" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-53"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-53 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-54" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-54"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-54 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-55" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-55"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-55 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-56" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-56"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-56 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-57" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-57"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-57 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-58" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-58"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-58 |
| streaming_cc.cc_trans_bridge:processing_cycle="13-59" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="13-59"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=13-59 |
| streaming_cc.cc_trans_bridge:processing_cycle="14-00" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="14-00"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-00 |
| streaming_cc.cc_trans_bridge:processing_cycle="14-01" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="14-01"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-01 |
| streaming_cc.cc_trans_bridge:processing_cycle="14-02" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="14-02"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-02 |
| streaming_cc.cc_trans_bridge:processing_cycle="14-03" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="14-03"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-03 |
| streaming_cc.cc_trans_bridge:processing_cycle="14-04" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="14-04"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-04 |
| streaming_cc.cc_trans_bridge:processing_cycle="14-05" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="14-05"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-05 |
| streaming_cc.cc_trans_bridge:processing_cycle="14-06" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="14-06"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-06 |
| streaming_cc.cc_trans_bridge:processing_cycle="14-07" | ALTER TABLE `streaming_cc`.`cc_trans_bridge` DROP IF EXISTS PARTITION (processing_cycle="14-07"); | mkdir -p hdfs://HOME90/user/nifi/datasets/external/cc_trans_bridge/processing_cycle=14-07 |

> truncated for brevity...