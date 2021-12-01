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

import com.cloudera.utils.hive.reporting.ReportingConf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class CollectStatistics implements Runnable {

    private JDBCRecordIterator jri;
//    private String header = null;
    private String comment = null;
    private Boolean tiktok = Boolean.FALSE;

    private DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private Long[] windows = {10000l, 30000l, 60000l, 180000l, 300000l};
    private Map<Long, PerfWindow> perfWindows = new TreeMap<Long, PerfWindow>();

    public JDBCRecordIterator getJri() {
        return jri;
    }

    public Long[] getWindows() {
        return windows;
    }

    public void setWindows(Long[] windows) {
        this.windows = windows;
    }

    public String getHeader() {
//        if (header == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("URL        : " + getJri().getJdbcUrl()).append("\n");
            sb.append("Batch Size : " + getJri().getBatchSize())
                    .append("(").append(getJri().getLastBatchSize()).append(")").append("\n");
            sb.append("SQL        : " + getJri().getQuery()).append("\n");
            sb.append("Lite       : " + getJri().getLite());
//            header = sb.toString();
//        }
        return sb.toString();
    }

//    public void setHeader(String header) {
//        this.header = header;
//    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CollectStatistics(JDBCRecordIterator jri) {
        this.jri = jri;
    }

    private void setUpWindows() {
        for (Long window : windows) {
            PerfWindow perfWindow = new PerfWindow(window, getJri());
            perfWindows.put(window, perfWindow);
        }
    }

    private void addToPerfWindows(Statistic statistic) {
        for (Long window : windows) {
            PerfWindow pw = this.perfWindows.get(window);
            pw.pushStat(statistic);
        }
    }

    private void updateStatus() {
        addToPerfWindows(getJri().getStat());
    }

    public void printStatus(Boolean finalIteration) {
        if (!finalIteration) {
            StringBuilder sb = new StringBuilder();
            sb.append(ReportingConf.CLEAR_CONSOLE);
            sb.append(ReportingConf.ANSI_YELLOW + "========== v.${Implementation-Version} ===========" + ReportingConf.ANSI_RESET).append("\n");
            sb.append(ReportingConf.ANSI_WHITE + getHeader()).append("\n");
            sb.append(ReportingConf.ANSI_YELLOW + "----------------------------" + ReportingConf.ANSI_CYAN).append("\n");
            sb.append(getJri().getConnectionDetails().toString()).append("\n");
            sb.append(ReportingConf.ANSI_YELLOW + "----------------------------" + ReportingConf.ANSI_GREEN);
            if (tiktok)
                sb.append("*\n");
            else
                sb.append("\n");
            sb.append("Window Length(ms) | Record Average | Records per/sec | Data Size per/sec | Fetch Time " + ReportingConf.ANSI_RESET).append("\n");
            for (Long window : windows) {

                sb.append(window);
                if (window < System.currentTimeMillis() -
                        getJri().getStart().getTime()) {
                    PerfWindow pw = this.perfWindows.get(window);
                    sb.append("\t\t" + pw.toString()).append("\n");
                } else {
                    sb.append("\t\t waiting for window to fill\n");
                }
            }
            sb.append(getJri().printLastFetchDelay()).append("\n");
            sb.append(getJri().printExcessiveFetchDelays()).append("\n");
            sb.append(ReportingConf.ANSI_BLUE + "===========================").append("\n");
            sb.append(ReportingConf.ANSI_YELLOW + "Running for: " + (System.currentTimeMillis() -
                    getJri().getStart().getTime()) + "ms\t\tStarted: " + dtf.format(getJri().getStart()) +
                    "\t\tRecord Count: " + getJri().getCount() + "\t\tData Size: " + getJri().getSize().get() + ReportingConf.ANSI_RESET).append("\n");

            String output = ReportingConf.substituteVariables(sb.toString());
            System.err.println(output);
            tiktok = !tiktok;
        } else {
            for (Long window : windows) {
                // Only print windows that have a full contingent of data
                if (window < System.currentTimeMillis() -
                        getJri().getStart().getTime()) {

                    StringBuilder sb = new StringBuilder();
                    String[] cparts = getComment().split(",");
                    for (String cpart : cparts) {
                        sb.append(cpart).append(",");
                    }
                    sb.append(getJri().getBatchSize()).append(",");
                    PerfWindow pw = this.perfWindows.get(window);
                    sb.append(window).append(",").append(pw.getAverage()).append(",")
                            .append(pw.getPerSec()).append(",").append(pw.getSizePerSec())
                            .append(",").append(pw.getFetchTime());
                    System.out.println(sb.toString());
                }
            }


        }

    }

//    @Override
//    public void interrupt() {
//        super.interrupt();
//    }

    @Override
    public void run() {
        setUpWindows();
        try {
            while (true) {
                TimeUnit.SECONDS.sleep(JDBCPerfTest.STATUS_INTERVAL_SECS);
//                Thread.sleep(JDBCPerfTest.STATUS_INTERVAL);
                updateStatus();
                if (getJri().getCompleted()) {
                    break;
                }
            }
        } catch (InterruptedException ire) {
            // Done
        }
    }
}
