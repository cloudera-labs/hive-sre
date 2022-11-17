## SRE Application (sre)

The Sre Tool brings together information from the HMS RDBMS and HDFS to provide reports and potential actions to address areas of concern.  This process is a READ-ONLY process and does not perform any actions automatically.

Action commands for identified scenarios are written out to file(s), which can be reviewed / edited and run through either "beeline" for "hive" actions or in [Hadoop-CLI](https://github.com/dstreev/hadoop-cli) for hdfs commands.

This process is driven by a control file.  A template is [here](configs/default.template.yaml).  Make a copy, edit the needed parameters and reference it with the '-cfg' parameter when running the process.

### Known Issues

For a while during the evolution of Hive 3, there was a separate 'catalog' for Spark.  The queries in this process do NOT consider this alternate catalog and may yield cross products in some areas if the 'spark' catalog was used at any point.  Due to the nature of this tool and an attempt to use it across multiple versions of Hive, we do NOT include criteria regarding the 'catalog'.

### Assumptions

- Ran from a node on the cluster
    - That includes clients and configuration files for HDFS
- Ran by a user that has READ privileges to all HDFS directories.
- If cluster is 'kerberized', the user has a valid Kerberos Ticket 'before' starting the application.
- Drivers for the HMS database are available.
- The configuration file has been defined
- The HMS Metastore DB is on a supported RDBMS for the platform (version matters!)

### Application Help

```
Launching: sre
usage: hive-sre u3|sre|perf -cdh|-hdp2|-hdp3|-all|-i <proc[,proc...]> -o <output-dir> [options]
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

The `-db` parameter is optional.  When specified, it will limit the search to the databases listed as a parameter.  IE: `-db my_db,test_db`

The `-o` parameter is *required*.

To limit which process runs, use the `-i` (include) option at the command line with a comma separated list of ids (below) of desired processes.

### `sre` Processes

| id (link to sample report)                                   | process |
|:-------------------------------------------------------------|:---|
| [1](./sample_reports/sre/hms_report_summary.md)              | Hive Metastore Summary<br/> - Numerous HMS reports outlining summary information about databases and tables |
| [2](./sample_reports/sre/hms_report_detail.md)               | Hive Metastore Details<br/> - Numerous HMS reports outlining detailed information about databases and tables |
| [3](./sample_reports/sre/small_files.md)                     | Table and Partition Scan - Small Files |
| [4](./sample_reports/sre/table_volume.md)                    | Table and Partition Scan - Volume Report |
| [5](./sample_reports/sre/empty_datasets.md)                  | Table and Partition Scan - Empty Datasets |
| [6](./sample_reports/u3/managed_compactions.sql)             | Table and Partition Compactions |
| [8](./sample_reports/sre/acid_analyze_tables.md)             | Analyze Tables (beta - use `-i 8` to activate) |
| [9](./sample_reports/sre/acid_analyze_tables_detailed.md)    | Analyze Tables - Detailed (beta - use `-i 9` to activate) |
| [10](./sample_reports/sre/hive_tbl_unique_base_dir_count.md) | Hive Table Type Base Location UNIQUE Count |
| [11](./sample_reports/sre/hive_tbl_anti_pattern_base_dir_count.md) | Hive Table Type Base Location Count Detail - Anti Pattern |

Sre needs to be run by a user with READ access to all the potential HDFS locations presented by the database/table/partition defined locations.
