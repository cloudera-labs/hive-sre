package com.cloudera.utils.hive.perf;

import java.util.Date;

public class StatsSummary {
    private Long recordCount = 0l;
    private Date start;
    private Date end;
    private Long window = 0l; // default of 0 means infinite.

    // The window increment used to display average
    private Long divisor = 1000l;

    public Long incrementRecordCount(Long increment) {
        recordCount =+ increment;
        return recordCount;
    }

    public Long getWindow() {
        return window;
    }

    public void setWindow(Long window) {
        this.window = window;
    }

    public Long getDivisor() {
        return divisor;
    }

    public void setDivisor(Long divisor) {
        this.divisor = divisor;
    }

//    public Long getAverage() {
//
//        return average;
//    }



}
