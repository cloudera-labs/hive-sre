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

package com.cloudera.utils.common;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TokenReplacementTest {

    @Test
    public void Test001() {
        TokenReplacement tr = TokenReplacement.getInstance();
        List<String> replacementText = new ArrayList<String>(
                Arrays.asList("SELECT id, ",
                        " -- this is a comment",
                        " town ",
                        "FROM",
                        "/**",
                        " multiline comment value 1",
                        " multiline comment  value 2",
                        "*/ ",
                        " city",
                        "WHERE",
                        " id = ${id}")
        );
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("id", 4);
        System.out.println(tr.replace(replacementText, properties));
    }

    @Test
    public void test002() {
        TokenReplacement tr = TokenReplacement.getInstance();
        List<String> replacementText = new ArrayList<String>(
                Arrays.asList("SELECT id, ",
                        " -- this is a comment",
                        " town ",
                        "FROM",
                        "/**",
                        " multiline comment value 1",
                        " multiline comment  value 2",
                        "*/ ",
                        " city",
                        "WHERE",
                        " id = ${id}")        );
        List<String> tokens = tr.listTokens(replacementText);
        System.out.println(tokens);
    }

    @Test
    public void test003() {
        TokenReplacement tr = TokenReplacement.getInstance();
        Map<String, Object> textMap = new HashMap<String, Object>();
        textMap.put("test-queue", "AND HQ.QUEUE_NAME = '${queue_name}'");
        textMap.put("test-queue2", "WHERE Something here");
        textMap.put("test-queue3", "Hello World");
        textMap.put("test-queue4", "ORDER BY ${order_field} DESC");
        List<String> tokens = tr.listTokens(textMap);
        System.out.println(tokens);
    }

    @Test
    public void test004() {
        TokenReplacement tr = TokenReplacement.getInstance();
        Map<String, Object> textMap = new HashMap<String, Object>();
        textMap.put("interval", "24h");
        textMap.put("and_queue", "AND HQ.QUEUE_NAME = '${queue_name}'");
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        tokenMap.put("queue_name", "finance");
        tokenMap.put("order_field", "request_user");

        Map<String, Object> fixedParamMap = tr.replaceInMap(textMap, tokenMap);

        System.out.println(fixedParamMap);
    }

}