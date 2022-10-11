/*
 * Copyright (c) 2022. Cloudera, Inc. All Rights Reserved
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.cloudera.utils.hive.config;

import com.cloudera.utils.hive.Sre;
import org.junit.Test;

public class SreSQLTest {

    @Test
    public void u3MYSQL_SQLTest() {
            String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                    ".hive-sre/cfg" + System.getProperty("file.separator") +
                    "default-" + Metastore.DB_TYPE.MYSQL + ".yaml";
            Sre.main(new String[]{"u3", "-tsql", "-all", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }

    @Test
    public void u3ORACLE_SQLTest() {
        String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                ".hive-sre/cfg" + System.getProperty("file.separator") +
                "default-" + Metastore.DB_TYPE.ORACLE + ".yaml";
        Sre.main(new String[]{"u3", "-tsql", "-all", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }

    @Test
    public void u3POSTGRESQL_SQLTest() {
        String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                ".hive-sre/cfg" + System.getProperty("file.separator") +
                "default-" + Metastore.DB_TYPE.POSTGRES + ".yaml";
        Sre.main(new String[]{"u3", "-tsql", "-all", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }

    @Test
    public void sreMYSQL_SQLTest() {
        String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                ".hive-sre/cfg" + System.getProperty("file.separator") +
                "default-" + Metastore.DB_TYPE.MYSQL + ".yaml";
        Sre.main(new String[]{"sre", "-tsql", "-all", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }

    @Test
    public void sreORACLE_SQLTest() {
        String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                ".hive-sre/cfg" + System.getProperty("file.separator") +
                "default-" + Metastore.DB_TYPE.ORACLE + ".yaml";
        Sre.main(new String[]{"sre", "-tsql", "-all", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }

    @Test
    public void srePOSTGRESQL_SQLTest() {
        String cfg = System.getProperty("user.home") + System.getProperty("file.separator") +
                ".hive-sre/cfg" + System.getProperty("file.separator") +
                "default-" + Metastore.DB_TYPE.POSTGRES + ".yaml";
        Sre.main(new String[]{"sre", "-tsql", "-all", "-cfg", cfg,"-o", "/tmp/sre-sql-test"});
    }


//    @Test
//    public void DefaultSQLTest() {
//        Sre.main(new String[]{"sre", "-tsql", "-o", "/tmp/sre-sql-test"});
//    }

}
