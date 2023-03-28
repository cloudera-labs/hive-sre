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

import static org.junit.Assert.*;

public class QueryResourceTest {

    public static final String DEFAULT_QUERY_DEF_RESOURCE = "/dba/" + DBAAnalysisFramework.QUERY_DEFS_RESOURCE;

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
        QueryResource qRsc = null;
        qRsc = loadFromResource(DEFAULT_QUERY_DEF_RESOURCE);
        System.out.println("Hello");
    }

    @Test
    public void loadResourceTest01() {
        QueryResource qRsc = null;
        qRsc = loadFromResource(DEFAULT_QUERY_DEF_RESOURCE);
        assertTrue(qRsc.loadResources());
        System.out.println("Hello");
    }

    public static QueryResource loadFromResource(String resource) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        QueryResource queryResource = null;
        URL configQRURL = mapper.getClass().getResource(resource);
        if (configQRURL != null) {
            String yamlConfigDefinition = null;
            try {
                yamlConfigDefinition = IOUtils.toString(configQRURL);
            } catch (IOException e) {
                throw new RuntimeException("Issue converting config: " + resource, e);
            }
            try {
                queryResource =
                        mapper.readerFor(QueryResource.class).readValue(yamlConfigDefinition);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Issue deserializing config: " + resource, e);
            }
        } else {
            throw new RuntimeException("Couldn't locate 'dba-Query Resource': " +
                    configQRURL.toString());
        }
//        queryResource.loadResources();
        return queryResource;
    }

}