/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.utils.mapreduce;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.mapreduce.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MRCommandInputFormat extends InputFormat<LongWritable, NullWritable> {

    private static final Log LOG = LogFactory.getLog(MRCommandInputFormat.class);

    public static final String NUM_ROWS = "repeat.command.count";

    /**
     * An input split consisting of a range on numbers.
     */
    static class MRCommandInputSplit extends InputSplit implements Writable {
        long firstRow;
        long rowCount;

        public MRCommandInputSplit() {
        }

        public MRCommandInputSplit(long offset, long length) {
            firstRow = offset;
            rowCount = length;
        }

        public long getLength() throws IOException {
            return 0;
        }

        public String[] getLocations() throws IOException {
            return new String[]{};
        }

        public void readFields(DataInput in) throws IOException {
            firstRow = WritableUtils.readVLong(in);
            rowCount = WritableUtils.readVLong(in);
        }

        public void write(DataOutput out) throws IOException {
            WritableUtils.writeVLong(out, firstRow);
            WritableUtils.writeVLong(out, rowCount);
        }
    }

    /**
     * A record reader that will generate a range of numbers.
     */
    static class MRCommandRecordReader
            extends RecordReader<LongWritable, NullWritable> {
        long startRow;
        long finishedRows;
        long totalRows;
        LongWritable key = null;

        public MRCommandRecordReader() {
        }

        public void initialize(InputSplit split, TaskAttemptContext context)
                throws IOException, InterruptedException {
            startRow = ((MRCommandInputSplit) split).firstRow;
            finishedRows = 0;
            totalRows = ((MRCommandInputSplit) split).rowCount;
        }

        public void close() throws IOException {
            // NOTHING
        }

        public LongWritable getCurrentKey() {
            return key;
        }

        public NullWritable getCurrentValue() {
            return NullWritable.get();
        }

        public float getProgress() throws IOException {
            return finishedRows / (float) totalRows;
        }

        public boolean nextKeyValue() {
            if (key == null) {
                key = new LongWritable();
            }
            if (finishedRows < totalRows) {
                key.set(startRow + finishedRows);
                finishedRows += 1;
                return true;
            } else {
                return false;
            }
        }

    }

    public RecordReader<LongWritable, NullWritable>
    createRecordReader(InputSplit split, TaskAttemptContext context)
            throws IOException {
        return new MRCommandRecordReader();
    }

    /**
     * Create the desired number of splits, dividing the number of rows
     * between the mappers.
     */
    public List<InputSplit> getSplits(JobContext job) {
        int numSplits = job.getConfiguration().getInt(MRJobConfig.NUM_MAPS, MRPingTool.DEFAULT_MAPPERS);
        long totalRows = getNumberOfRows(job) * numSplits;
        LOG.info("Generating " + totalRows + " using " + numSplits);
        List<InputSplit> splits = new ArrayList<InputSplit>();
        long currentRow = 0;
        for (int split = 0; split < numSplits; ++split) {
            long goal =
                    (long) Math.ceil(totalRows * (double) (split + 1) / numSplits);
            splits.add(new MRCommandInputSplit(currentRow, goal - currentRow));
            currentRow = goal;
        }
        return splits;
    }

    static long getNumberOfRows(JobContext job) {
        return job.getConfiguration().getLong(NUM_ROWS, MRPingTool.DEFAULT_COUNT);
    }

    static void setNumberOfRows(Job job, long numRows) {
        job.getConfiguration().setLong(NUM_ROWS, numRows);
    }

}
