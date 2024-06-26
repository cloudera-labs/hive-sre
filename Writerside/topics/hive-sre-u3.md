# Hive Upgrade Check (u3)

The `u3` sub-command is a process that is used to review 'Hive 1/2' environments for Hive3 upgrade planning.

Review Hive Metastore Databases and Tables for upgrade or simply to evaluate potential issues.  The intent was to make that process much more prescriptive and consumable by Cloudera customers.  The application is 'Apache Hive' based, so it should work against both 'HDP', 'CDH', and 'CDP' clusters.  See additional details about [upgrading HDP2 and CDP 5/6 to CDP](hive-sre-to-cdp.md).

## Quick Start (What to do!!!)

This process can be a long running process.  I recommend using `screen` or `tmux` sessions to run it.  That way the session won't get terminated when you disconnect from the host and you can easily reattach later to see the progress.

The process should start registering counts pretty quickly for each of the sub-checks.  If you do not see these counts moving, or they show `0`, check your connection to the Metastore database (IE: URL, RDBMS Driver(needs to be in $HOME/.hive-sre/aux_libs), Username, password, and/or permissions). `hive-sre` versions 2.4.0.21.0 and above will catch some of these misconfigurations and exit quickly.

Logs for the application are at $HOME/.hive-sre/logs.  Check those for details on progress and/or problems.

### Use the Platform flag to run ONLY the important reports.

Use one of `-cdh`, `-hdp2`, or `-hdp3` depending on your source platform to run only the reports that make sense.  If you don't use one of these options, that's fine.  Just know that you'll be running MORE reports than you need and the process will run much longer.  Just saying....

EACH report contains a description **within** the report on what actions should be taken.

If you are running the default upgrade and NOT doing the CDP Expedited Hive Upgrade Process for [CDH5](https://docs.cloudera.com/cdp-private-cloud-upgrade/latest/upgrade-cdh/topics/hive-expedite-cdh-overview.html), [CDH6](https://docs.cloudera.com/cdp-private-cloud-upgrade/latest/upgrade-cdh6/topics/hive-expedite-cdh-overview.html), [HDP2](https://docs.cloudera.com/cdp-private-cloud-upgrade/latest/upgrade-hdp/topics/hive-expedite-hdp-overview.html) there is a HSMM (Hive Strict Managed Migration Process) that runs AFTER the binaries and components have been upgraded to CDP's Hive 3.  This process (HSMM) will fail if the issues/errors reported in reports [1 - Table / Partition Location Scan - Missing Directories](hive-sre-u3-loc_scan_missing_dirs.md),  [5 - Questionable Serde's Check](hive-sre-u3-hms_checks.md) are not fixed BEFORE doing the upgrade.

Metadata Updates, like [3 - Hive 3 Upgrade Checks - Managed Non-ACID to ACID Table Migrations](hive-sre-u3-managed_upgrade_2_acid.md) are done after the components and configurations have been updated to CDP.  Therefore, the report actions for [3 - Hive 3 Upgrade Checks - Managed Non-ACID to ACID Table Migrations](hive-sre-u3-managed_upgrade_2_acid.md) and [6 - Provide Script for ACID tables missing the 'bucketing_version' property](hive-sre-u3-acid_bucketing_update.md)  will be done by HSMM.

If you are doing the Expedited Hive Upgrade process there are two options:
1. Skip the HSMM process altogether.  This means you will need to run the actions from the following, manually. These actions can be done BEFORE the upgrade, BUT may change behaviors expected in the legacy platform.  They can be done AFTER the upgrade where there isn't a behavior impact.  These will take time and dependent on the number of actions you need to perform.  The tables must be updated BEFORE they can be consumed by users.  But this doesn't mean you need to hold the upgrade or increase the downtime of the cluster.  Organized high priority DBs first and work on less accessed DBs later.
    1. [3 - Hive 3 Upgrade Checks - Managed Non-ACID to ACID Table Migrations](hive-sre-u3-managed_upgrade_2_acid.md)
    2. [6 - Provide Script for ACID tables missing the 'bucketing_version' property](hive-sre-u3-acid_bucketing_update.md)
2. Use the hsmm_includelist.yaml produced by `u3` to direct HSMM towards targeted actions. See the Expedite Hive Upgrade docs for details.

#### HDP2

Runs reports:

- [1](hive-sre-u3-loc_scan_missing_dirs.md) - Table / Partition Location Scan - Missing Directories
- [3](hive-sre-u3-managed_upgrade_2_acid.md) - Hive 3 Upgrade Checks - Managed Non-ACID to ACID Table Migrations
- [4](hive-sre-u3-managed_compactions.md) - Hive 3 Upgrade Checks - Compaction Check
- [5](hive-sre-u3-hms_checks.md) - Hive Metastore Check <br/> - Questionable Serde's Check<br/>  - Questionable Serde's Check<br/> - Leagcy Kudu Serde Report<br/> - Legacy Decimal Scale/Precision Check<br/> - List Databases with Table/Partition Counts
- [6](hive-sre-u3-acid_bucketing_update.md) - Provide Script for ACID tables missing the 'bucketing_version' property
- 7 - List tables using known Storage Handlers
- 8 - Hive 3 Upgrade Checks - List Managed Non-ACID tables that need to be converted. (Run `u3e` to process)
- 9 - List ACID tables missing the 'bucketing_version' property (Run `u3e` to process)

#### HDP3

Runs reports:
- [1](hive-sre-u3-loc_scan_missing_dirs.md) - Table / Partition Location Scan - Missing Directories
- [5](hive-sre-u3-hms_checks.md) - Hive Metastore Check <br/> - Questionable Serde's Check<br/>  - Questionable Serde's Check<br/> - Leagcy Kudu Serde Report<br/> - Legacy Decimal Scale/Precision Check<br/> - List Databases with Table/Partition Counts
- [6](hive-sre-u3-acid_bucketing_update.md) - Provide Script for ACID tables missing the 'bucketing_version' property

#### CDH

Runs reports:

- [1](hive-sre-u3-loc_scan_missing_dirs.md) - Table / Partition Location Scan - Missing Directories
- [3](hive-sre-u3-managed_upgrade_2_acid.md) - Hive 3 Upgrade Checks - Managed Non-ACID to ACID Table Migrations
- [5](hive-sre-u3-hms_checks.md) - Hive Metastore Check <br/> - Questionable Serde's Check<br/>  - Questionable Serde's Check<br/> - Leagcy Kudu Serde Report<br/> - Legacy Decimal Scale/Precision Check<br/> - List Databases with Table/Partition Counts
- [6](hive-sre-u3-acid_bucketing_update.md) - Provide Script for ACID tables missing the 'bucketing_version' property
- 7 - List tables using known Storage Handlers
- 8 - Hive 3 Upgrade Checks - List Managed Non-ACID tables that need to be converted. (Run `u3e` to process)
- 9 - List ACID tables missing the 'bucketing_version' property (Run `u3e` to process)

## Testing

I have tested this against MariaDB 10.2, Postgres, and Oracle.  I have seen reports of SQL issues against MySql 5.6, so this process will certainly have issues there.  If you run this in other DB configs, I would like to hear from you about it.

## Known Issues

For a while during the evolution of Hive 3, there was a separate 'catalog' for Spark.  The queries in this process do NOT consider this alternate catalog and may yield cross products in some areas if the 'spark' catalog was used at any point.  Even though this tool was designed for Hive 1 and 2 in mind, it can be run against Hive 3.  We do NOT include criteria regarding the 'catalog'.

## Assumptions

- Ran from a node on the cluster
    - That includes clients and configuration files for HDFS
- Ran by a user that has READ privileges to all HDFS directories.
- If cluster is 'kerberized', the user has a valid Kerberos Ticket 'before' starting the application.
- Drivers for the HMS database are available.
- The configuration file has been defined
- The HMS Metastore DB is on a supported RDBMS for the platform (version matters!)

## Application Help

```
usage: hive-sre u3|sre|perf -cdh|-hdp2|-hdp3|-all|-i <proc[,proc...]> [options]
                version:2.4.0.24.0-SNAPSHOT
Hive SRE Utility
 -all,--all-reports                            Run ALL available processes.
 -cdh,--cloudera-data-hub                      Run processes that make sense for CDH.
 -cfg,--config <arg>                           Config with details for the Sre Job.  Must match the
                                               either sre or u3 selection. Default:
                                               $HOME/.hive-sre/cfg/default.yaml
 -db,--database <arg>                          Comma separated list of Databases.  Will override
                                               config. (upto 100)
 -dbRegEx,--database-regex <arg>               A RegEx of databases to process
 -dp,--decrypt-password <encrypted-password>   Used this in conjunction with '-pkey' to decrypt the
                                               generated passcode from `-p`.
 -edbRegEx,--exclude-database-regex <arg>      A RegEx that will filter OUT matching databases from
                                               processing
 -h,--help                                     Help
 -hdp2,--hortonworks-data-platfrom-v2          Run processes that make sense for HDP2.
 -hdp3,--hortonworks-data-platfrom-v3          Run processes that make sense for HDP3.
 -hfw,--hive-framework <arg>                   The custom HiveFramework check configuration. Needs
                                               to be in the 'Classpath'.
 -i,--include <arg>                            Comma separated list of process id's to run.  When
                                               not specified, ALL processes are run.
 -o,--output-dir <arg>                         Output Directory to save results from Sre.
 -p,--password <password>                      Used this in conjunction with '-pkey' to generate the
                                               encrypted password that you'll add to the configs for
                                               the JDBC connections.
 -pkey,--password-key <password-key>           The key used to encrypt / decrypt the cluster jdbc
                                               passwords.  If not present, the passwords will be
                                               processed as is (clear text) from the config file.
 -scc,--skip-command-checks                    Don't process the command checks for the process.
 -tsql,--test-sql                              Check SQL against target Metastore RDBMS

Visit https://github.com/cloudera-labs/hive-sre for detailed docs
 ```

To limit which process runs, use the `-i` (include) option at the command line with a comma separated list of ids (below) of desired processes.

## `u3` Processes

| id (link to sample report)                           | process |
|:-----------------------------------------------------|:---|
| [1](hive-sre-u3-loc_scan_missing_dirs.md)            | Table / Partition Location Scan - Missing Directories |
| [2](hive-sre-u3-bad_filenames_for_orc_conversion.md) | Table / Partition Location Scan - Bad ACID ORC Filenames |
| [3](hive-sre-u3-managed_upgrade_2_acid.md)           | Hive 3 Upgrade Checks - Managed Non-ACID to ACID Table Migrations |
| [4](hive-sre-u3-managed_compactions.md)              | Hive 3 Upgrade Checks - Compaction Check |
| [5](hive-sre-u3-hms_checks.md)                       | Hive Metastore Check <br/> - Questionable Serde's Check<br/>  - Questionable Serde's Check<br/> - Leagcy Kudu Serde Report<br/> - Legacy Decimal Scale/Precision Check<br/> - List Databases with Table/Partition Counts |
| [6](hive-sre-u3-acid_bucketing_update.md)                                               | Provide Script for ACID tables missing the 'bucketing_version' property |

## Running

Run the **Hive Metastore Checks** Report first (`-i 5`) to get a list of databases with table and partition counts.  With this information, you can develop a strategy to run the tool in parts using the `-db` option.  Multi-tasking can be controlled in the configuration files `parallelism` setting.

To ease the launch of the application below, configure these core environment variables.

## Output

The output is a set of files with actions and error (when encountered).  The files maybe `txt` files or `markdown`.  You may want to use a `markdown` viewer for easier viewing of those reports.  The markdown viewer needs to support [github markdown tables](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#tables) .

### Examples

All command assume the config file is here: `$HOME/.hive-sre/cfg/default.yaml`

*All Hive Databases* - Will run processes relevant to `cdh`.
```
hive-sre u3 -cdh -o ./sre-out
```

*Targeted Hive Database* - Will run ALL processes on the 'priv_dstreev' hive database and run only reports relative to `hdp2`.
```
hive-sre u3 -db priv_dstreev -hdp2 -o ./sre-out
```

*Run ONLY compaction check on All Hive Database* - Using the `-i` option to run only the 'compaction check' sub routine.
```
hive-sre u3 -o ./sre-out -i 4
```


## Getting Ready for a Hive 1/2 to Hive 3 Upgrade

The reports created by this application will provide you with a prescriptive set of actions that will prepare and accelerate the Hive 1/2 to Hive 3 upgrade.

Hive consists of two parts:  Metadata and Data.  Since these are not strictly linked, they do get out of sync.  The process 'Location Scans' above finds tables and partitions that don't have a matching file system location.  Use the details of this report to 'clean up' the metastore and filesystem.

Use the reports and scripts provided to build a plan for your upgrade.  The latest versions of `u3` will create a file called `hsmm_includelist.yaml`.  This contains a list of Databases and Tables that need attention.  Use the contents of this list to choose the actions you'll take for the upgrade.  The choices are:

- Run the HiveStrictManagedMigration process against these targeted database and tables to avoid its default iteration over ALL DB's and tables.
- Run the scripts suggested by the reports to migrate your tables.  NOTE: Scripts aren’t included to address legacy Kudu storage handler classes (CDH Only).  Run the HSMM process on those DB’s and Tables identified in the report.

Note that the HiveStrictManagedMigration process DOES make adjustments to database definitions as a part of the upgrade.  The CDP upgrade docs will include examples on how to ensure that process is run against the DB's and not any tables, to minimize the time in that process. 