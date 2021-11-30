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