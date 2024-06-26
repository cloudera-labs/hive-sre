# Upgrading to CDP

CDP 7.x+ is running Hive 3.  Platforms running Hive 1/2 (CDH 5/6 and HDP 2) need to upgrade/convert tables so they are compatible with Hive 3.

The most significant adjustment is the definition of a "Managed Table".  In Hive 1/2, a managed table is defined as "Drop the table, drop the data."  In this state, a managed table will maintain the tables state in the 'metastore' AND the 'filesystem'.  In Hive 3, a "Managed Table" has been elevated and is a transactional table (ACID).

Review the table below for details on what 'should' be converted to what.

## Conversion Matrix

| Legacy Table Type | New Table Type | CDH Recommendation | HDP Recommendation |
|:---|:---|:---|:---|
| External | External | External | External |
| Managed (non-transaction) | External / Purge | N/A | External / Purge [^1] |
| Managed (Transactional) | Managed (Transactional) | N/A | Managed (Transactional) |

## General Recommendation

**Do NOT migrate "Legacy Managed" tables to "Managed Transactional" tables.**  This is not meant to dissuade you away from using ACID/Transactional table in anyway.  It has been our experience that ACID tables support certain access methods which isn't compatible

### For CDH (5 and 6) and HDP 2 Clusters

The [Cloudera upgrade documentation for CDH clusters](https://docs.cloudera.com/cdp-private-cloud/latest/upgrade-cdh/topics/ug_hive_validations.html) outlines a series of steps to reduce the downtime and hasten the 'Hive' upgrade process by skipping the detailed evaluation of 'every' table in the metastore.  But, in contrast, the work STILL need to be done.  The `hive-sre` tool generates scripts (below) that make up for the things skipped.

If you follow the CDP docs to 'expedite' the hive upgrade process, these steps can be done 'post' upgrade BEFORE you release the cluster to the community.  Process [1](hive-sre-u3-loc_scan_missing_dirs.md) (missing directories) can be done BEFORE the upgrade, and may even reduce the number of items captured in the other steps.

Run the [`u3`](hive-sre-u3.md) process against all your databases and __act__ upon the results from process ID's:
- [1](hive-sre-u3-loc_scan_missing_dirs.md) - Missing Directories
- [3](hive-sre-u3-managed_upgrade_2_acid.md) - Managed Table Conversions
- [5](hive-sre-u3-hms_checks.md) - Kudu, Serdes, Decimal scale/precision issues


[^1]: Beware! Default upgrade process MAY convert these tables to ACID tables.  This is NOT recommended and should be mitigated to prevent this. Use the scripts generated by process [3](hive-sre-u3-managed_upgrade_2_acid.md) to mitigate this. 