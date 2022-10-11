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

package com.cloudera.utils.hive.sre;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class UtilsTest {

    @Test
    public void dirToPartitionSpec_001() {
        String[] testSet = {"st=GA A/update_dt=2020-09-03"};

        for (String test: testSet) {
            String spec = null;
            try {
                spec = Utils.dirToPartitionSpec(test);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(spec);
        }
    }

    @Test
    public void decodeTS_001() {
        String[] testSet = {"st=GA A/update_dt=2019-09-01 12%3A31%3A44.333"};

        for (String test: testSet) {
            String spec = null;
            try {
                spec = Utils.dirToPartitionSpec(test);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(spec);
        }
    }
    @Test
    public void decodeTS_002() {
        String[] testSet = {"update_dt=2009-10-23/batch=test"};

        for (String test: testSet) {
            String spec = null;
            try {
                spec = Utils.dirToPartitionSpec(test);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(spec);
        }
    }

    @Test
    public void partitionCheck_001() {
        String[] testSet = {"ingest_partition=20200411123432"};

        for (String test: testSet) {
            String spec = null;
            try {
                spec = Utils.dirToPartitionSpec(test);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(spec);
        }
    }
}