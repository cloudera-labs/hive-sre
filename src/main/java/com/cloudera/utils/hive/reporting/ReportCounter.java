/*
 * Copyright 2021 Cloudera, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.utils.hive.reporting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

//@JsonIgnoreProperties({"totalCount", "processed", "success", "error"})
public class ReportCounter {

    private String name = null;
    private String currentTask = "na";

    private Map<TaskState, AtomicLong> counts = new TreeMap<TaskState, AtomicLong>();

    public ReportCounter(String name) {
        this.name = name;
    }

    public String getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(String currentTask) {
        this.currentTask = currentTask;
    }

    private List<ReportCounter> children = new ArrayList<ReportCounter>();

    public List<ReportCounter> getChildren() {
        return children;
    }

    public long incCount(TaskState state, int increment) {
        AtomicLong counter = counts.get(state);
        if (counter == null) {
            counter = new AtomicLong(0);
            counts.put(state, counter);
        }
        return counter.addAndGet(increment);
    }

    public Map<TaskState, AtomicLong> getCounts() {
        return counts;
    }

    public long getCount(TaskState state) {
        AtomicLong count = counts.get(state);
        if (count != null)
            return count.get();
        else
            return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
