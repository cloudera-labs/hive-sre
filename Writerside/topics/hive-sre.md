# Introduction

The `hive-sre` application was designed to help with the analysis of Hive Metastore and HDFS data.  

The [u3](hive-sre-u3.md) sub-command is used mainly during the pre-planning phase of a major environment upgrade (HDP->CDP,CDH->CDP) to understand the scope of changes needed for CDP.

The [sre](hive-sre-sre.md) sub-command is used to run a series of checks against the Hive Metastore and HDFS data to identify potential issues that would affect query/system performance and stability.

It is a Java application that connects directly to the Hive Metastore RDBMS and executes queries to gather information about the Hive Metastore and compares that with HDFS data.

## Supported Metastore DB's

| Sub-Program | Database        | Version | Tested                           | Notes                                                                                                                            |
|:------------|:----------------|:--------|:---------------------------------|:---------------------------------------------------------------------------------------------------------------------------------|
| `u3`        | MySql           | 5.6     | Limited                          | Recommend upgrading 5.7.  This is the lower MySql supported env for HDP                                                          |
|             |                 | 5.7     | Yes                              |  |
|             |                 | 8.0     | No                               | Not supported by HDP                                                                                                             |
|             | MariaDb         | 10.1    | No, but should work as 10.2 does |                                                                                                                                  |
|             |                 | 10.2    | Yes                              |                                                                                                                                  |
|             | Postgresql      | 9.6     | No, but should work              |                                                                                                                                  |
|             |                 | 10      | Yes                              | Field Tested, May still be a few rough edges                                                                                     |
|             |                 | 11      | No, but should work at 10 does   |                                                                                                                                  |
|             | Oracle          | 12      | Yes                              | Field Tested, May still be a few rough edges                                                                                     |
| `u3e`       | MySql           | 5.6     | Limited                          | Recommend upgrading 5.7.  This is the lower MySql supported env for HDP                                                          |
|             |                 | 5.7     | Yes                              |  |
|             |                 | 8.0     | No                               | Not supported by HDP                                                                                                             |
|             | MariaDb         | 10.1    | No, but should work as 10.2 does |                                                                                                                                  |
|             |                 | 10.2    | Yes                              |                                                                                                                                  |
|             | Postgresql      | *       | NOT YET IMPLEMENTED              |                                                                                                                                  |
|             | Oracle          | *       | NOT YET IMPLEMENTED              |                                                                                      |
| `sre`       | MySql           | 5.6     | Limited                          | Recommend upgrading 5.7.  This is the lower MySql supported env for HDP                                                          |
|             |                 | 5.7     | Partly                           |   Some `sre` reports use CTE in the SQL, which isn't supported in this version.  Those report will error, the other will run fine.                                                                                                                               |
|             |                 | 8.0     | No                               | Not supported by HDP                                                                                                             |
|             | MariaDb         | 10.1    | No, but should work as 10.2 does |                                                                                                                                  |
|             |                 | 10.2    | Yes                              |                                                                                                                                  |
|             | Postgresql      | 9.6     | No, but should work              |                                                                                                                                  |
|             |                 | 10      | Yes                              | Field Tested, May still be a few rough edges                                                                                     |
|             |                 | 11      | No, but should work at 10 does   |                                                                                                                                  |
|             | Oracle          | 12      | Yes                              | Field Tested, May still be a few rough edges                                                                                     |

Ensure you have the database appropriate driver in the `${HOME}/.hive-sre/aux_libs` directory.

I've tried to match supported DB's for HDP 2.6.5 and 3.1.x as much as I could.

## Hadoop CLI

Running `hive-sre-cli` on the command line is an alias to the `hadoopcli` application [here](https://github.com/dstreev/hadoop-cli).

It is an interactive HDFS Client Command Line tool.

## Ping Performance Tool

Included in this application suite is a `ping` performance tool that you can use to measure cluster host letancy.  It is a MapReduce application that uses a list of hosts and will `ping` those hosts from the MR Map Task and record the results to HDFS.

The MR program take a few options:
```
usage: hadoop jar <jar-file> com.cloudera.utils.mapreduce.MRPingTool -d <output-dir> -hl <hdfs_host_list_file> [-c <count>] [-m <num_of_mappers>]
 -c,--count <arg>        Ping Count (The number of iterations we'll run
                         ping).  Each ping request makes 5 pings.
                         So if this value is 3, we'll do 3 sets of 5 pings
                         (15 total). Default 5
 -d,--directory <arg>    Output Directory [REQUIRED]
 -h,--help               Help
 -hl,--host-list <arg>   The host list file on HDFS. A text file with a
                         FQHN (full qualified host name) per
                         line.[REQUIRED]
 -m,--mappers <arg>      Number of Mappers.  To get coverage, should be
                         more than the compute node count.  But no
                         guarantee of even distribution, so best to over
                         subscribe. Default 2
```

### Tested OS's

CentOS 7.6 (ping)

The ping output is parsed by the application and other OS/versions may yield different output, which we've not (yet) setup parsing for.

### Let's assume:
- You've downloaded the Hive [setup/ingest/eval scripts](https://github.com/cloudera-labs/hive-sre/tree/main/src/main/resources/ping)
- This has been tested again CentOS 7.
- The user running the MR job has rights in each compute node to run `ping`.
- That `ping` is allowed on the network and not blocked between hosts.
- The MR Job is run with the right user credentials and from an Edgenode configured for the target cluster.
    - We need the hadoop MR libs to submit the job.
- The user running the job has ACL's that allow them to write to the EXTERNAL db location you create with `ping_ddl.sql`.
- Build a 'host list' file with ALL the FQHN (fully qualified hostnames) in the cluster that you want to test.
    - This is a text file with a single host FQHN per line.  Similar to /etc/hosts.
    - Place the file (we'll use the name `host_list.txt`) in your HDFS home directory.
- We are using the standard EXTERNAL and MANAGED Warehouse locations on HDFS
- PING_DB = ping_perf
- BATCH_ID = 2022-10-22_01

### Procedure
1. Run DDL Scripts in Beeline
    2. `beeline -f ping_ddl.sql --hivevar PING_DB=ping_perf`
3. Run MR Job, using the expected Partition Directory of the `raw` table as the output. The `-m` parameter controls the number of 'mappers' created.  The intent is to get a map task on every compute node in the cluster.  To do that, with perfect distribution (unlikely), you need at least as many mappers as there are compute nodes.  We recommend 2-3x that incase some nodes get doubled up.  There's no way to ensure every host runs a task, hence the over subscription here.  Use the eval report to determine where the gaps where (if any) and run again.
    4. `hadoop jar /usr/local/hive-sre/lib/hive-sre-shaded.jar com.cloudera.utils.mapreduce.MRPingTool -m 40 -c 3 -hl host_list.txt -d /warehouse/tablespace/external/hive/ping_perf.db/raw/batch_id=2022-10-21_01`
5. Run the Ingest script in `beeline`
    6. `beeline -f ping_ingest.sql --hivevar PING_DB=ping_perf --hivevar BATCH_ID=2022-10-21_01`
7. Run the [eval scripts](https://github.com/cloudera-labs/hive-sre/blob/main/src/main/resources/ping/ping_eval.sql) in `beeline --hivevar PING_DB=ping_perf --hivevar BATCH_ID=2022-10-21_01`


