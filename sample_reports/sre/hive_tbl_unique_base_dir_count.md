# Hive Table Location - Anti-Pattern (v.2.4.0.21.0-SNAPSHOT)

This report will show which databases have more than 1 base directory per table type.

 This is important to identify because the replication process for 'external' data will increase in complexity with each unique instance.

 When there's more than one 'managed' location per database, this is probably the result of legacy locations.  Hive 3 doesn't allow locations for managed tables to be set, the database (MANAGEDLOCATION) or the metastore warehouse directory will dictate the location.  It is possible to change the 'MANAGEDLOCATION' for a database which will change 'newly' created tables only.

 Further ACL's need to be maintained at the FS level to manage the unique locations.

 Best practices encourage table type datasets to share a common base location to reduce complexity and administrative ACL overhead and reduce the number of file system replication policies required to manage a 'dr' solution.

| DB | Table Type | Unique Count |
|:---|:---|:---| 
| wellcare | MANAGED_TABLE | 2 |
| z_hms_mirror_testdb_20220720_195237 | MANAGED_TABLE | 2 |
