## Troubleshooting

### Kerberos Connectivity Issues

Older JDK's that are a part of the environment `PATH` may cause issues when connecting to the services.  The following parameters will help debug those sessions.

Add Kerberos Java debugging parameters to the commandline.

```
hive-sre -Dsun.security.krb5.debug=true u3 -o sre-out
hive-sre -Dsun.security.krb5.debug=true sre -o sre-out
```

or

```
hive-sre-cli -Dsun.security.krb5.debug=true
```

One thing to check, before debugging Kerberos is that you are able to connect with a basic `hdfs dfs -ls /` hadoop cli command.  If the environment is kerberized AND you have a valid ticket, the `hdfs` command should work.

Assuming that works and you are still having issues with `hive-sre`, than check that you are running the same JDK for `hive-sre` as is setup for the `hdfs` cli.

Our `hdfs` client implementation used in `hive-sre` requires the authenticated user to have a HOME directory in `hdfs`.  IE: If your prinicipal is for `david` then there should be an hdfs directory `/user/david`.  If there isn't a user HOME directory in HDFS, the application may hang on initialization.

The application will display the JDK version at startup.  Use this to confirm.

### Setting up $JAVA_HOME

If the 'default' jdk isn't the one being used by the cluster, set the `$JAVA_HOME` environment variable BEFORE starting `hive-sre`.  When this is set, `hive-sre` will use that JDK.

### Setting an alternate HADOOP_CONF_DIR

By default, 'hive-sre' uses the `/etc/hadoop/conf` directory to locate the environment configurations.  If you have an alternate directory, you can override this by setting the env variable `HADOOP_CONF_DIR`.

```
export HADOOP_CONF_DIR=/my/hadoop/conf_dir
```

If you see the following message, it means the needed configurations we not found.

```
Invalid URI for NameNode address (check fs.defaultFS): file:/// has no authority. cmd:org.apache.commons.cli.CommandLine...
```

Ideally, the host should be configured as an **HDFS Gateway** in Cloudera Manager.

### `java.sql.SQLException: The server time zone value '***' is unrecognized or represents more than one time zone.`

This happens when running against *MySQL* and there is a translation issue with the timezone set on the server.  Override this with the client by adding the following to the jdbc connection url: `serverTimezone=UTC`.

For example: `jdbc:mysql://m01.streever.local:3306/hive_50?serverTimezone=UTC`

### 'No Progress Exception'

This happens when a connection is usually made but the application fails to make progress initializing.  The check was added for certain scenarios that hung the application indefinitely.

Under some conditions, the connection may take a bit longer to establish and this process kicks in and kills the application.  Sometimes a "High-Availability" RDBMS might have just enough of a delay to trigger this process.  In that case, you can extend the time given for the check by adding:

`reportingInterval: 1000`

to the configuration yaml file.  Add it before or after the `parallelism` setting at the level indent level.