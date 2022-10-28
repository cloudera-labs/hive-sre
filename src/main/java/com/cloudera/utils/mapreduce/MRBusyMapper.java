/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.utils.mapreduce;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MRBusyMapper extends Mapper<LongWritable, NullWritable, NullWritable, Text> {

    public void map(LongWritable key, NullWritable value, Context context) throws IOException, InterruptedException {
        Text lineText = new Text(RandomStringUtils.random(100));
        // Wait for 'busyWait' seconds.
        TimeUnit.SECONDS.sleep(context.getConfiguration().getInt(MRBusyTool.PAUSE_INCREMENT, MRBusyTool.PAUSE_INCREMENT_DEFAULT));
        context.write(NullWritable.get(), lineText);
    }

}
