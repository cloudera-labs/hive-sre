# Configuration

Using this template, create a configuration with details about your environment.

The `metastore_direct` refers to the JDBC connection to the Hive Metastore RDBMS.  It is NOT a reference to Hive JDBC (Hive Server 2). This process goes directly against the metastore DB.

For `ORACLE`, notice the `initSql`, this is required to set the schema.

Use the `parallelism` option to control the number of threads run by the process.  This shouldn't exceed 125% of the number of cores on the host you're running this from.

When running `sre` or `u3` you can either include the `-cfg <cfg_file>` option in the commandline OR create and store the configuration in the file `$HOME/.hive-sre/cfg/default.yaml`.  This is the default location and used automatically when present when the  `-cfg` option isn't specified.


## Default or Alt JDK Usage

The startup script `hive-sre|hive-sre-cli` will use `$JAVA_HOME` if defined.  When it is not available, the default `java` implementation will be used.

JDK's below 1.8.0_100 are not recommended for Kerberos environments and may have issues connecting to secure clusters.  Those JDK's require additional `unlimited jce` configurations.

If kerberos connections aren't working, use a more recent JDK by setting the `$JAVA_HOME` variable.

## AUX_LIBS - CLASSPATH Additions

The directory `$HOME/.hive-sre/aux_libs` will be scanned for 'jar' files. Each 'jar' will be added the java classpath of the application.  Add any required libaries here.

The application contains all the necesasry `hdfs` classes already.  You will need to add to the `aux_libs` directory the following:
- JDBC drivers for Metastore Connectivity
- AWS S3 Drivers, if s3 is used to store Hive tables. (appropriate versions)
    - `hadoop-aws.jar`
    - `aws-java-sdk-bundle.jar`
    
