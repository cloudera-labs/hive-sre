package com.cloudera.utils.hive.config;

import com.cloudera.utils.hive.Sre;
import org.junit.Test;

public class SreSQLTest {

    @Test
    public void u3MYSQL_SQLTest() {
            String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                    ".hive-sre/cfg" + System.getProperty("file.separator") +
                    "default-" + Metastore.DB_TYPE.MYSQL + ".yaml";
            Sre.main(new String[]{"u3", "-tsql", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }

    @Test
    public void u3ORACLE_SQLTest() {
        String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                ".hive-sre/cfg" + System.getProperty("file.separator") +
                "default-" + Metastore.DB_TYPE.ORACLE + ".yaml";
        Sre.main(new String[]{"u3", "-tsql", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }

    @Test
    public void u3POSTGRESQL_SQLTest() {
        String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                ".hive-sre/cfg" + System.getProperty("file.separator") +
                "default-" + Metastore.DB_TYPE.POSTGRES + ".yaml";
        Sre.main(new String[]{"u3", "-tsql", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }

    @Test
    public void sreMYSQL_SQLTest() {
        String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                ".hive-sre/cfg" + System.getProperty("file.separator") +
                "default-" + Metastore.DB_TYPE.MYSQL + ".yaml";
        Sre.main(new String[]{"sre", "-tsql", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }

    @Test
    public void sreORACLE_SQLTest() {
        String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                ".hive-sre/cfg" + System.getProperty("file.separator") +
                "default-" + Metastore.DB_TYPE.ORACLE + ".yaml";
        Sre.main(new String[]{"sre", "-tsql", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }

    @Test
    public void srePOSTGRESQL_SQLTest() {
        String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                ".hive-sre/cfg" + System.getProperty("file.separator") +
                "default-" + Metastore.DB_TYPE.POSTGRES + ".yaml";
        Sre.main(new String[]{"sre", "-tsql", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }


//    @Test
//    public void DefaultSQLTest() {
//        Sre.main(new String[]{"sre", "-tsql", "-o", "/tmp/sre-sql-test"});
//    }

}
