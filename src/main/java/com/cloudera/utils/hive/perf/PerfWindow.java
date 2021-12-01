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

package com.cloudera.utils.hive.perf;

import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PerfWindow {

    private Long windowLength;
    private Statistic first, last;
    private Long realWindowLengthSecs;
    private JDBCRecordIterator jri;

    private Deque<Statistic> queue = new ConcurrentLinkedDeque<Statistic>();

    public JDBCRecordIterator getJri() {
        return jri;
    }

    public synchronized void pushStat(Statistic stats) {
        queue.add(stats);
        checkQueue(stats.getTimestamp());
        first = queue.getFirst();
        last = queue.getLast();
        realWindowLengthSecs = (last.getTimestamp().getTime() - first.getTimestamp().getTime())/1000;
    }

    private void checkQueue(Date checkPoint) {
        while (true) {
            Statistic stats = queue.getFirst();
            if (stats.getTimestamp().getTime() < checkPoint.getTime() - windowLength) {
                queue.removeFirst();
            } else {
                break;
            }
        }
    }

    public PerfWindow(Long windowLength, JDBCRecordIterator jri) {
        if (windowLength == null) {
            throw new RuntimeException("Need to specify Window Length (milliseconds)");
        }
        this.windowLength = windowLength;
        this.jri = jri;
    }

    public Long getAverage() {
//        Statistic first = queue.getFirst();
//        Statistic last = queue.getLast();
        Long total = last.getRecordCount() - first.getRecordCount();
        return total;
    }

    public Long getPerSec() {
//        Statistic first = queue.getFirst();
//        Statistic last = queue.getLast();
        Long total = last.getRecordCount() - first.getRecordCount();
        return total / realWindowLengthSecs;
    }

    public Long getSizePerSec() {
//        Statistic first = queue.getFirst();
//        Statistic last = queue.getLast();
        Long total = last.getSize() - first.getSize();
        return total / realWindowLengthSecs;
    }

    public Long getFetchTime() {
        Iterator<FetchDelay> iFetchs = getJri().getFetchDelays().descendingIterator();

        Long rtn = 0l;
        while (iFetchs.hasNext()) {
            FetchDelay fetch = iFetchs.next();
            if (fetch.getTimestamp() > first.getTimestamp().getTime()) {
                rtn += fetch.getDelay();
            } else {
                break;
            }
        }
        return rtn;
    }

    @Override
    public String toString() {
        Long perInterval = getAverage();
        Long perSecond = getPerSec();
        StringBuilder sb = new StringBuilder();
//        sb.append("WindowAverage: ");
        sb.append(getAverage());
        sb.append("\t\t");
//        sb.append("\tPer/Sec: ");
        sb.append(getPerSec());
        sb.append("\t\t");
//        sb.append("\tSize/Sec: ");
        sb.append(getSizePerSec());
        sb.append("\t\t");
//        sb.append("\tSize/Sec: ");
        sb.append(getFetchTime());
        return sb.toString();
    }
}
