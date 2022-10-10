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

package com.cloudera.utils.hive.reporting;

import com.cloudera.utils.hive.config.NoProgressException;
import com.cloudera.utils.hive.sre.SreMessages;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

public class CounterGroup {
    private int initCheckRetry = 0;
    private static final int initCheckRetryThreshold = 5;

    private static Logger LOG = LogManager.getLogger(CounterGroup.class);

    private String name = null;

    private List<ReportCounter> counters = new ArrayList<ReportCounter>();

    private Map<TaskState, AtomicLong> taskState = new TreeMap<TaskState, AtomicLong>();

    public CounterGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void checkInitialized() throws NoProgressException {
        long constructed = getTaskStateValue(TaskState.CONSTRUCTED);
        if (constructed == 0) {
            initCheckRetry++;
            if (initCheckRetry > initCheckRetryThreshold) {
                throw new NoProgressException(SreMessages.INIT_ISSUE);
            }
        }
    }

    public long addAndGetTaskState(TaskState state, long value) {
        AtomicLong ctr = taskState.get(state);
        if (ctr == null) {
            ctr = new AtomicLong(value);
            taskState.put(state, ctr);
        } else {
            ctr.addAndGet(value);
        }
        return ctr.get();

    }

    public long decrementTaskState(TaskState state) {
        AtomicLong ctr = taskState.get(state);
        if (ctr == null) {
            ctr = new AtomicLong(0);
            taskState.put(state, ctr);
        }
        return ctr.decrementAndGet();
    }

    public Map<TaskState, AtomicLong> getTaskState() {
        return taskState;
    }

    public long getTaskStateValue(TaskState state) {
        AtomicLong lclState = taskState.get(state);
        if (lclState != null)
            return lclState.get();
        else
            return 0l;
    }

    public void setTaskState(Map<TaskState, AtomicLong> counters) {
        this.taskState = counters;
    }

    public List<ReportCounter> getCounters() {
        return counters;
    }

    public void setCounters(List<ReportCounter> counters) {
        this.counters = counters;
    }

}
