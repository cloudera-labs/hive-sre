## Missing Filesystem Directories

Listed in the `hive-sre u3 -i 1` report, which is run with the `-hdp2` and `-cdh` flags. The artifact from the report is `loc_scan_missing_dirs.md` which lists which tables/partitions are missing directories on the filesystem.

CDP Hive3+ has more strict requirements regarding the consistency between 'metadata' and the 'filesystem'.  When tables or partitions are access via Hive SQL and the underlying directory is missing, the queries will fail with a "VERTEX" issue.

This process is a metastore hygiene process.  It is not a requirement to run this process immediately (but can cause issues with tables when accessed).  

It is a recommendation that as much of this 'hygiene' be done BEFORE the upgrade during the planning and prep phases of the upgrade.  This way you aren't caught up in the urgency of the moment while upgrading and turning the cluster back over to the users.

There are three solutions to this issue.  The solutions aren't necessarily mutually exclusive to the entire listing of missing directories.

1. Create the missing directories on the filesystem.
2. Remove the metadata from the Hive Metastore where there is no corresponding directory on the filesystem.
   a. Via Hive SQL
   b. Directly from the Hive Metastore DB (Only MySQL supported currently)
3. Sync the Partitions in hive via `MSCK REPAIR TABLE <table_name> SYNC PARTITIONS`.

### 1. Create the missing directories on the filesystem

This is the most straightforward solution.  The directories are missing on the filesystem, so create them.  This can be done via the `hdfs dfs -mkdir <path>` command.  The directories should be created with the same permissions as the other directories in the same path.

This solution is the easiest to implement, but it doesn't address the root cause of the issue.  The root cause is that the metadata in the Hive Metastore is out of sync with the filesystem.  This can be a symptom of a larger issue.  The larger issue is that the Hive Metastore is not being maintained.  The Hive Metastore is a critical component of the Hive ecosystem.  It is the 'source of truth' for the Hive ecosystem.  If the Hive Metastore is not being maintained, then the Hive ecosystem is not being maintained.

In our experience, when a lot of these missing directories are identified, it is the result of a development pattern where users create tables in hive and clean up data in HDFS without cleaning up the metadata in the Hive Metastore.  This is a bad practice and should be discouraged.  The Hive Metastore should be maintained as part of the data lifecycle.  If the data is removed from HDFS, then the metadata should be removed from the Hive Metastore.

So simply creating these directories without understanding how the table/partition ended up this way, creates more 'trash' in the system.  For example: An application practice is to create a table, load some external data to HDFS and ingest the data into another final table.  As time passes, the source data isn't required, so an effort to cleanup the filesystem is undertaken and the data removed.  Leaving the original source table/partition definitions in place.  Adding back the directories may lead to false positives on the state of those tables/partitions.

#### Pros
* Easy to implement

#### Cons
* Doesn't address the root cause of the issue.
* Creates a lot of 'trash' on the file system.
* May lead to false positives on the state of the tables/partitions.

**Optimizing the process using `hive-sre-cli`**
This is an HDFS client installed with `hive-sre`.  It can be used to run a set of hdfs commands within the same JVM, therefore eliminating the JVM startup time from each command if you ran it via `hdfs dfs ...`.  This can be a significant time savings if you have a large number of directories to create.

All the same requirements are needed to run `hive-sre-cli` as are needed for `hdfs dfs ...`.  You need to be on a node with an HDFS Gateway installed and you need to be able to authenticate to the cluster via Kerberos (kinit) or as the logged in user when not kerberized.

Use grep and awk to generate a list of commands and directories to create from the `loc_scan_missing_dirs.md` file.  The following command will generate a list of commands to create the directories.

```bash
# Parse the loc_scan_missing_dirs.md file and generate a list of commands to create the directories.
grep '^|' loc_scan_missing_dirs.md | awk -F'|' '{print $4}' | grep '^ mkdir' >  mkdirs.txt
# Use the hive-sre-cli to run the commands in the mkdirs.txt file.
hive-sre-cli -f mkdirs.txt
```

If you want to filter for certain databases (eg: test_db) before running this it could look something like this:

```bash
grep '^| test_db.*' loc_scan_missing_dirs.md | awk -F'|' '{print $4}' | grep '^ mkdir' >  mkdirs.txt
hive-sre-cli -f mkdirs.txt
```

### 2. Remove the metadata from the Hive Metastore where there is no corresponding directory on the filesystem

#### 2.a Via Hive SQL 
This is a more involved solution.  The directories are missing on the filesystem, so remove the metadata from the Hive Metastore.  This can be done via the `hive -e "ALTER TABLE <table_name> DROP PARTITION (<partition_name>);"` command.  This will remove the metadata from the Hive Metastore.

This solution however can be quite time consuming.  For a more expedited option along this line, see option 2.a

**Pros**
* Cleans up the Hive Metastore.
* Doesn't create 'trash' on the filesystem.
* Doesn't lead to false positives on the state of the tables/partitions.

**Cons**
* Time consuming.

**Build the Hive SQL Alter Commands**
```bash
grep  '^|' loc_scan_missing_dirs.md | awk -F'|' '{print $3}' | grep '^ ALTER' >  hive_alter.sql
```

#### 2.b Directly from the Hive Metastore DB (Only MySQL supported currently)

This process currently only support 'PARTITIONS', not tables.  Once complete, run the reports again and use `Hive SQL` to drop the tables that are now empty.

This solution is the quickest solution available to apply these changes in bulk. The newer report version include an `id` for the partitions that are missing locations.  Use the `mysql_missing_parts.sh` script to generate the SQL to remove the partitions from the Hive Metastore.  The output is a sql script that can be run directly against the Hive Metastore DB.

`mysql_missing_parts.sh loc_scan_missing_dirs.md`

This creates 1+ MySQL script `mysql_missing_parts.sql` that can be run directly against the Hive Metastore DB.  The script are broken down into 1000 line chunks.  This is to prevent the script from being too large to run.  The script can be run via the `mysql` command line tool.

**Altering the partitions it picks up**
You can likewise use grep against the `loc_scan_missing_dirs.md` file to filter the partitions you want to remove.  For example, if you only want to remove partitions from a specific database, you can use the following sequence of commands:

```
# Filter out the 'test_db' hive database
grep '^| test_db.*' loc_scan_missing_dirs.md > trimmed.md

# Generates OR appends to the mysql_missing_parts.sql script.
mysql_missing_parts.sh trimmed.md
```

**Running the script**
MAKE A BACKUP OF THE METASTORE!!!  Seriously.. This script is destructive.  It will remove partitions from the Hive Metastore.  If you run this script against the wrong database, you could cause a lot of damage.  So make a backup of the metastore before running this script.

Copy sql file to system with a mysql client installed.  Run the script via the `mysql` command line tool. You'll need to specify the database in MySql containing the metastore db and use a user with permissions to modify the metastore db.

### 3. Sync the Partitions in hive via `MSCK REPAIR TABLE <table_name> SYNC PARTITIONS`

This solution can only be run in CDP.  `SYNC PARTITIONS` isn't always supported in legacy Hive environments.  So this is a POST upgrade task.  

You can choose to run these commands right AFTER the upgrade in CDP BEFORE you turn the system over to the users, or you can choose to run these commands in an ADHOC manner as users report issues.  In fact, users with the right permissions can run these commands themselves on tables they are using, if they experience the VERTEX issue due to a missing directory.

Although, the `MSCK` process can take a while IF the table has a large number of partitions to reconcile.

Choosing to run these commands across ALL candidates POST upgrade can take a significant amount of time. Missing directories are only an issue when user try to access them, so creating a system down/unavailable to repair tables that may never be accessed is a waste of time.  Knowing the scope of the issue is important and working with the users to educate them on reconciling their tables could save a lot of time and allow you to turn-over the system asap.

**Build the Hive SQL MSCK Commands**
```bash
grep  '^|' loc_scan_missing_dirs.md | awk -F'|' '{print $5}' | grep '^ MSCK' >  hive_msck.sql
```