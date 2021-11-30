package com.cloudera.utils.hive.reporting;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

public class CounterGroup {
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
