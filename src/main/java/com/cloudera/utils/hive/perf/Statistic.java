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

public class Statistic {
    private Date timestamp;
    private Long recordCount;
    private Long size = 0l;
    private Long delay = 0l;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Long getDelay() {
        return delay;
    }

    public static Statistic build(Long recordCount) {
        Statistic stats = new Statistic();
        stats.setTimestamp(new Date());
        stats.setRecordCount(recordCount);
        return stats;
    }

    public static Statistic build(Long recordCount, Long size) {
        Statistic stats = new Statistic();
        stats.setTimestamp(new Date());
        stats.setRecordCount(recordCount);
        stats.setSize(size);
        return stats;
    }

    public static Statistic build(Long recordCount, Long size, Long delay) {
        Statistic stats = new Statistic();
        stats.setDelay(delay);
        stats.setTimestamp(new Date());
        stats.setRecordCount(recordCount);
        stats.setSize(size);
        return stats;
    }

}
