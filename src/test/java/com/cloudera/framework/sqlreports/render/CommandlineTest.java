/*
 * Copyright (c) 2023. Cloudera, Inc. All Rights Reserved
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

package com.cloudera.framework.sqlreports.render;

import com.cloudera.framework.sqlreports.NavigationTreeTest;
import com.cloudera.framework.sqlreports.QueryResourceTest;
import com.cloudera.framework.sqlreports.SessionContext;
import com.cloudera.utils.hive.config.DBStore;
import com.cloudera.utils.hive.dba.QueryStore;

public class CommandlineTest {

//    @Test
    public static void main(String[] args) {
        SessionContext sc = SessionContext.getInstance();
        QueryStore qStore = new QueryStore();
        qStore.setType(DBStore.DB_TYPE.POSTGRES);
        sc.setQueryStore(qStore);

        sc.setQueryResource(QueryResourceTest.loadFromResource(QueryResourceTest.DEFAULT_QUERY_DEF_RESOURCE));
        sc.setNavigationTree(NavigationTreeTest.loadFromResource(NavigationTreeTest.DEFAULT_NAV_RESOURCE));
//        CommandLine cl = new CommandLine();
//        cl.listen();
    }
}