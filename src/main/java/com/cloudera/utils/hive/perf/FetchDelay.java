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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FetchDelay {
    private static final DateFormat dtf;

    static {
        dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    private Long timestamp;
    private Long delay;
    private Long marker;

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getMarker() {
        return marker;
    }

    public void setMarker(Long marker) {
        this.marker = marker;
    }

    public FetchDelay(Long delay, Long marker) {
        this.timestamp = System.currentTimeMillis() - delay;
        this.delay = delay;
        this.marker = marker;
    }

    @Override
    public String toString() {
        return  " -> At " + dtf.format(timestamp) + " there was a excessive delay of " + delay +
                " after " + marker + " records.";
    }
}
