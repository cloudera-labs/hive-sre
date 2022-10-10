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
