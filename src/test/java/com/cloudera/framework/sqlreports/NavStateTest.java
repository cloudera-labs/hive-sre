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

package com.cloudera.framework.sqlreports;

import com.cloudera.utils.hive.config.DBStore;
import com.cloudera.utils.hive.dba.QueryStore;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class NavStateTest {

    @Before
    public void setUp() throws Exception {
        if (SessionContext.getInstance().getQueryStore() == null) {
            QueryStore queryStore = new QueryStore();
            queryStore.setType(DBStore.DB_TYPE.POSTGRES);
            SessionContext.getInstance().setQueryStore(queryStore);
        }
    }

    @Test
    public void qTest01() {
        Deque<String> tl = new ArrayDeque<String>();
        tl.offer("A");
        tl.offer("B");
        assertTrue(tl.peekLast().equals("B"));
        assertTrue(tl.pollLast().equals("B"));
        tl.offer("C");
        assertTrue(tl.getLast().equals("C"));
    }

    @Test
    public void NavStateTest_001() {
        SessionContext sc = SessionContext.getInstance();
        sc.setQueryResource(QueryResourceTest.loadFromResource(QueryResourceTest.DEFAULT_QUERY_DEF_RESOURCE));
        sc.setNavigationTree(NavigationTreeTest.loadFromResource(NavigationTreeTest.DEFAULT_NAV_RESOURCE));
        assertTrue(sc.getQueryResource().loadResources());
        assertTrue(sc.getNavigationTree().loadResources());

        NavState ns = sc.getState();

    }
}