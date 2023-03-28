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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class NavigationTreeTest {
    public static final String DEFAULT_NAV_RESOURCE = "/dba/" + DBAAnalysisFramework.NAV_TREE_RESOURCE;

    @Before
    public void setUp() throws Exception {
        if (SessionContext.getInstance().getQueryStore() == null) {
            QueryStore queryStore = new QueryStore();
            queryStore.setType(DBStore.DB_TYPE.POSTGRES);
            SessionContext.getInstance().setQueryStore(queryStore);
        }
    }

    @Test
    public void loadTest01() {
        NavigationTree navTree = null;
        navTree = loadFromResource(DEFAULT_NAV_RESOURCE);
        System.out.println("Hello");
    }

    @Test
    public void test1() {
        int i = 4;
        i += 3;
        System.out.println(i);
    }

    public static NavigationTree loadFromResource(String resource) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        NavigationTree navTree = null;
        URL configNavURL = mapper.getClass().getResource(resource);
        if (configNavURL != null) {
            String yamlConfigDefinition = null;
            try {
                yamlConfigDefinition = IOUtils.toString(configNavURL);
            } catch (IOException e) {
                throw new RuntimeException("Issue converting config: " + resource, e);
            }
            try {
                navTree =
                        mapper.readerFor(NavigationTree.class).readValue(yamlConfigDefinition);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Issue deserializing config: " + resource, e);
            }
        } else {
            throw new RuntimeException("Couldn't locate 'dba-Navigation Tree': " +
                    configNavURL.toString());
        }
        return navTree;
    }
}