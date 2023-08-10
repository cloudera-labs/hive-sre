## Hive 3 Upgrade Execution Process

> This process is meant to run **AFTER** the upgrade and before the system is release for consumer access.  It will work against the metastore DB directly, which is much faster than running the previous HiveSQL scripts for each table.  You __can__ run this against the older Hive versions, but it may render some tables in a state that is NOT compatible with Hive 1/2.
> 
> We suggest running this process against a 'copy' of the metastore db to understand the timings for upgrade planning.

Upgrade to CDP can run this process instead of the 'full' HSMM process run during the upgrade.  See the [Hive Expedited Upgrade](https://docs.cloudera.com/cdp-private-cloud-upgrade/latest/upgrade-cdh/topics/hive-expedited-migration-tasks.html).  If you run this `u3e` process, most of the work will be done already.  Create an empty includes file for HSMM and add it to the configuration as directed in the link above.  This will ensure any 'database' changes are also applied.

At this time, this process is ONLY available to MySQL/MariaDB backend metastore DB's.

After running the `u3` process to review the extent of the changes, you can run this script **AFTER** the upgrade to Hive 3 to make the changes that cover:

- Legacy Managed Non-Transactional (non-acid) Conversions to EXTERNAL/PURGE
  - Will convert all LEGACY MANAGED tables to EXTERNAL/PURGE
- ACID Table bucketing version update
  - Adds the correct bucket version property to ACID tables.
- Legacy Contrib Serde2 Replacement
  - Handles the conversions of:
    - `org.apache.hadoop.hive.contrib.serde2.RegexSerDe`
    - `org.apache.hadoop.hive.contrib.serde2.TypedBytesSerDe`
    - `org.apache.hadoop.hive.contrib.serde2.MultiDelimitSerDe`
  - Other Serdes identified in the `u3` process will need to be independently addressed.

These scripts will execute directly against the METASTORE DB, skipping the HiveSQL interface.  This process is considerably faster than processing these requests through the HiveSQL interface 1 at a time. 

### This process does NOT address

- Kudu Serde / Table Upgrades.  See: https://kudu.apache.org/docs/hive_metastore.html
- Legacy Decimal (Hive 0.12) issues.  Refer to description of these in the `u3` report.
