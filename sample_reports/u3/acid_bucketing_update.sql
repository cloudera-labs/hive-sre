-- Basic Processing (Skip Command Check specified)
ALTER TABLE default.legacy_acid_01 SET TBLPROPERTIES('bucketing_version'='2');
ALTER TABLE tpcds_bin_partitioned_orc_10.call_center_acid SET TBLPROPERTIES('bucketing_version'='2');
ALTER TABLE tpcds_bin_partitioned_orc_10.web_sales_acid SET TBLPROPERTIES('bucketing_version'='2');
ALTER TABLE z_hms_mirror_testdb_20220720_195237.acid_01 SET TBLPROPERTIES('bucketing_version'='2');
ALTER TABLE z_hms_mirror_testdb_20220720_195237.acid_02 SET TBLPROPERTIES('bucketing_version'='2');
ALTER TABLE z_hms_mirror_testdb_20220720_195237.acid_03 SET TBLPROPERTIES('bucketing_version'='2');
