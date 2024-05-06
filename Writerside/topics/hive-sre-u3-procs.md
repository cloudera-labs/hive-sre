# Checks and Validations Processes

`hive-sre` has the following checks and validations.  Use one of `-hdp2`, `-hdp3`, `-cdh`, `-all`, or `-i <process_ids>` to run the checks.

Below is the list of checks and validations. The process ID is the indexed number below, if you wish to run with the `-i` option.

## 1. Hive 3 Upgrade Checks - Locations Scan
    - Missing Directories
   > Missing Directories cause the upgrade conversion process to fail.  To prevent that failure, there are two choices for a 'missing directory'.  Either create it of drop the table/partition.

    - You have two choices based on the output of this process.
        - RECOMMENDED: Drop the table/partition OR
        - Create the missing directory referenced by the table/partition (empty directories have been known to cause hive table CBO issues, leading to performance slowdowns).
    - The output file from this process will provide commands to accomplish which ever direction you choose. Use 'hive' to run the sql statements.  Use [hadoopcli](https://github.com/dstreev/hadoop-cli) to run the 'hdfs' commands in bulk.
## 2. Hive 3 Upgrade Checks - Bad ORC Filenames
    - Bad Filename Format
   > Tables that would be convert from a Managed Non-Acid table to an ACID transactional table require the files to match a certain pattern. This process will scan the potential directories of these tables for bad filename patterns.  When located, it will indicate which tables/partitions have been file naming conventions that would prevent a successful conversion to ACID.  The best and easiest way to correct these files names is to use HiveSQL to rewrite the contents of the table/partition with a simple 'INSERT OVERWRITE TABLE xxx SELECT * FROM xxx'.  This type of statement will replace the current bad filenames with valid file names by rewriting the contents in HiveSQL.
## 3. Hive 3 Upgrade Checks - Managed Table Migrations
    - Ownership Check
    - Conversion to ACID tables
   > This process will list tables that will and 'could' be migrated to "Managed ACID" tables during the upgrade process.  If these tables are used by Spark OR data is managed by a separate process that interacts with the FileSystem, DO NOT LET THESE conversion happen.  The output of this process will supply Hive DDL commands to convert these tables to "EXTERNAL / PURGE" tables in Hive 3, which is the same as the 'classic' Hive 1/2 Managed Non-Acid table.
## 4. Hive 3 Upgrade Checks - Compaction Check
    - Compaction Check
   > Review ACID tables for 'delta' directories.  Where 'delta' directories are found, we'll
## 5. Metastore Report
    - Questionable Serde's Check
   > Will list tables using SERDE's that are not standard to the platform.
    - Action here either:
        - Remove the table using the SERDE, if the SERDE isn't available
        - Ensure the SERDE is available during the upgrade so table can be evaluated.
    - Legacy Kudu Serde Report
      > Early versions of Hive/Impala tables using Kudu were built before Kudu became an Apache Project.  Once it became an Apache Project, the base Kudu Storage Handler classname changed.  This report locates and reports on tables using the legacy storage handler class.
    - Legacy Decimal Scale and Precision Check
      > When the `DECIMAL` data type was first introduced in Hive 1, it did NOT include a Scale or Precision element.  This causes issues in later integration with Hive and Spark.  We'll identify and suggest corrective action for tables where this condition exists.
    - Managed Table Shadows
   > In Hive 3, Managed tables are 'ACID' tables.  Sharing a location between two 'ACID' tables will cause compaction issues and data issues.  These need to be resolved before the upgrade.
    - Database / Table and Partition Counts
   > Use this to understand the scope of what is in the metastore.
## 6. Hive ACID tables missing 'bucketing_version'='2' tblproperties
- This report will show the ACID tables that don't contain the required 'bucketing_version=2' tblproperties configuration.  This is required for the upgrade to Hive 3.
  -- Without this setting, you will have issues access/reading/processing the upgraded ACID table.
  -- We recommend that you run the script to add this configuration value to the tables.
## 7. Hive Storage Handlers
If any of these storage handlers show up as a 'MANAGED_TABLE' type and you are upgrading from a legacy (Hive 1/2) platform, the 'Managed Non-ACID to ACID Table Migrations' process will pick them up for conversion.  If the storage handlers location information is NOT valid or the Hive table is orphaned, the upgrade will fail while attempting to convert the table and affect your ability to complete the upgrade process for Hive.\n\n Ensure the location for 'MANAGED_TABLE' Storage Handler backed tables is valid AND available.
# 8. Legacy Managed Non-Transactional (non-acid) Conversions to EXTERNAL/PURGE
This is a list of tables that meet the LEGACY Managed non-transactional criteria.  Use this to understand the scope of the change.

Run the `u3e` process to execute the fixes for this list of tables.
# 9. Hive ACID tables missing 'bucketing_version'='2' tblproperties
This report will show the ACID tables that don't contain the required 'bucketing_version=2' tblproperties configuration. Without this setting, you will have issues access/reading/processing the upgraded ACID table. 

We recommend that you run the `u3e` process to correct after the upgrade.
