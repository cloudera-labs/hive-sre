# Pre-Requisites

1. Access to the metastore RDBMS for the cluster you're working on.  This would be either the MySql, PostgreSQL, or Oracle RDBMS instance.
2. Read access to 'hdfs' for the cluster you're working on.  This is required to review and validate table locations on the filesystem.
3. Permissions to run the hive-sre SQL output generated in the reports.
4. Use a current version of the JDBC driver for the metastore RDBMS.  Place this in the `aux_libs` directory.
5If there are tables in S3, ensure the AWS S3 drivers are available in the `aux_libs` directory and the 'gateway' config has been configured with the S3 credentials.
5. Host should have JDK 8 installed.  If it's not the default JDK, set the `$JAVA_HOME` variable to the appropriate JDK before running `hive-sre`.

## Before Running

For kerberized clusters, ensure the user running `hive-sre` has a valid kerberos ticket.  This is required to connect to HDFS.
