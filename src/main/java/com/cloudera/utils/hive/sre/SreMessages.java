package com.cloudera.utils.hive.sre;

public interface SreMessages {
    String INIT_ISSUE = "hive-sre doesn't seem to be making progress.  Please check:\n" +
            "\t1. Your settings to the Metastore RDBMS (url, username, password, RDBMS permissions)\n" +
            "\t\t(a) The url should contain the metastore db in the connect string for MySql.\n" +
            "\t\t\tPostgreSql depends on the connection user.  Review PostgreSQL JDBC connection strings\n" +
            "\t\t\tOracle requires a session statement, see default config templates 'initSql' example\n" +
            "\t2. Ensure the 'user' running 'hive-sre' has permissions to the filesystem \n" +
            "\t3. User running 'hive-sre' has valid kerberos ticket (if cluster is kerberized) to read ALL of hdfs\n" +
            "\t4. The RDBMS JDBC jar file is in $HOME/.hive-sre/aux_libs\n" +
            "\t5. If you specified a db with `-db`, ensure that it/they are valid 'hive' databases.\n" +
            "\t6. If you are using an encrypted password, you need to specify `-pkey` with the decrypt key to connect.\n" +
            "\t7. Review the config docs: https://github.com/cloudera-labs/hive-sre#configuring-hive-sre";
}
