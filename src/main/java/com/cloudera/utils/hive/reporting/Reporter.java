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

import com.cloudera.utils.hive.config.NoProgressException;
import com.cloudera.utils.hive.sre.ProcessContainer;
import com.cloudera.utils.hive.sre.SreMessages;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

public class Reporter implements Runnable {

    private int WIDTH = 100;
    private int linePos = 0;
    private boolean tictoc = false;
    private int refreshInterval = 500;

    private int ERROR_POS = 0;
    private int SUCCESS_POS = 1;
    private int TOTAL_POS = 2;

    private String name;
    private Date startTime = new Date();
    private ProcessContainer processContainer;

    private Map<String, CounterGroup> counterGroups = new TreeMap<String, CounterGroup>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public void addCounter(CounterGroup counterGroup, ReportCounter counter) {
        if (counterGroups.containsKey(counterGroup.getName())) {
            if (counter != null)
                counterGroups.get(counterGroup.getName()).getCounters().add(counter);
        } else {
            if (counter != null)
                counterGroup.getCounters().add(counter);
            counterGroups.put(counterGroup.getName(), counterGroup);
        }
    }

    public ProcessContainer getProcessContainer() {
        return processContainer;
    }

    public void setProcessContainer(ProcessContainer processContainer) {
        this.processContainer = processContainer;
    }

    private void pushLine(String line) {
        linePos += 1;
        System.out.println(line);

    }

    private void resetLines() {
        System.out.print(ReportingConf.CLEAR_CONSOLE);
        linePos = 0;
    }

    // Return true when not completed.
    public boolean refresh() {
        boolean rtn = getProcessContainer().isActive();
        String INDENT = "    ";

        // Prevent the Reporter Thread from terminating too quickly
        if (processContainer.isInitializing()) {
            return true;
        }

        resetLines();
        long totalTaskCount = 0l;
        try {
            StringBuilder version = new StringBuilder();
            version.append(ReportingConf.ANSI_YELLOW);
            version.append(ReportingConf.substituteVariables("v.${Implementation-Version}") + " - DB Mapping for: " +
                    this.getProcessContainer().getConfig().getMetastoreDirect().getType().toString());
            version.append(ReportingConf.ANSI_RESET);

            if (!tictoc) {
                version.append(" *");
            }
            version.append("\t");
            String threadStatus = processThreadStatus(getProcessContainer().getTaskThreadPool());
            version.append(threadStatus);

//            version.append(getProcessContainer().getThreadPool().getActiveCount());
            pushLine(version.toString());

            Deque<String> lines = new LinkedList<>();
            // For the proc id
            for (String groupName : this.counterGroups.keySet()) {

                CounterGroup counterGroup = counterGroups.get(groupName);


                // The CommandChecks for the Proc
                if (counterGroup.getCounters().size() > 0) {
                    for (ReportCounter ctr : counterGroup.getCounters()) {

                        Map<TaskState, AtomicLong> ccrProgress = new LinkedHashMap<TaskState, AtomicLong>();
                        for (Map.Entry<TaskState, AtomicLong> entry: ctr.getCounts().entrySet()) {
                            ccrProgress.put(entry.getKey(), entry.getValue());
                            totalTaskCount += entry.getValue().get();
                        }
//                        for (TaskState state : TaskState.values()) {
//                            procProgress.get(state).addAndGet(ctr.getCount(state));
//                            jobProgress.get(state).addAndGet(ctr.getCount(state));
//                        }

                        // TODO: Handle CheckCalc Reporting...  But we aren't reporting here yet..

                        lines.offerFirst(progressCount(10, ctr.getName(), "", ccrProgress));
//                        lines.offerFirst(progressCount(10, ctr.getName(), "", null));

//                        StringBuilder ccrSb = new StringBuilder();
//                        ccrSb.append(ctr.getName()).append(StringUtils.leftPad(progressCount(ccrProgress))
//                        String ccrStr = ctr.getName();
//
//                        Wrap cd = getCounterDisplay(INDENT, ctr);
//                        currentProcessing.addAll(cd.details);

                    }
                    lines.offerFirst(progressCount(counterGroup));
                    lines.offerFirst(StringUtils.leftPad(ReportingConf.ANSI_CYAN, WIDTH, "-"));
                } else {
                    lines.offerFirst(progressCount(counterGroup));
                    lines.offerFirst(StringUtils.leftPad(ReportingConf.ANSI_CYAN, WIDTH, "-"));
                }


            }
            lines.add(StringUtils.leftPad(ReportingConf.ANSI_CYAN, WIDTH, "="));
//            lines.add(progressCount(0, "Job Totals:", "", jobProgress));

            Iterator<String> d = lines.iterator();
            while (d.hasNext()) {
                pushLine(d.next());
            }

//            pushLine(StringUtils.leftPad("=", WIDTH, "="));

            Date now = new Date();

            Long elapsedSec = (now.getTime() - startTime.getTime()) / 1000;

            try {
                StringBuilder jobTotalsSb = new StringBuilder("Velocity:");
                jobTotalsSb.append(ReportingConf.ANSI_BLUE).append("   (").append(elapsedSec).append("/")
                        .append(totalTaskCount / elapsedSec).append(")").append(ReportingConf.ANSI_RESET);

                String jobSummary = jobTotalsSb.toString();
                jobSummary = StringUtils.leftPad(jobSummary, WIDTH - (jobSummary.length() + 1), " ");
                pushLine(jobSummary);
            } catch (ArithmeticException ae) {

            }
        } catch (NoProgressException npe) {
//            pushLine("Calculating Tasks");
//            System.err.println(npe.getMessage());
            npe.printStackTrace();
            System.exit(-1);
//            t.printStackTrace();
        }
        tictoc = !tictoc;
        return rtn;
    }

    protected String processThreadStatus(ThreadPoolExecutor threadPool) {
        StringBuilder sb = new StringBuilder();
        sb.append(threadPool.getCorePoolSize()).append(",");
        sb.append(threadPool.getLargestPoolSize()).append(",");
        sb.append(threadPool.getMaximumPoolSize()).append(" - ");
        sb.append(threadPool.getActiveCount()).append(",");
        sb.append(threadPool.getCompletedTaskCount()).append(",");
        sb.append(threadPool.getQueue().size()).append(",");
        sb.append(threadPool.getTaskCount());
        return sb.toString();
    }

    protected String progressCount(int indent, String name, String filler, Map<TaskState, AtomicLong> counts) {
        StringBuilder sb = new StringBuilder();
        String prefix = ReportingConf.ANSI_GREEN + StringUtils.leftPad(name, indent + name.length(), filler);

        if (counts != null) {
            sb.append(ReportingConf.ANSI_BLUE).append("[").append(ReportingConf.ANSI_GREEN);
            Iterator keyItr = counts.keySet().iterator();
            while (keyItr.hasNext()) {
                AtomicLong count = counts.get(keyItr.next());
                sb.append(count.get());
                if (keyItr.hasNext()) {
                    sb.append("/");
                }
            }
            sb.append(ReportingConf.ANSI_BLUE).append("]");
        }
        String ctrStr = sb.toString();
        return prefix + StringUtils.leftPad(ctrStr, WIDTH - prefix.length() - indent, filler);
    }

    protected String progressCount(CounterGroup counterGroup) throws NoProgressException {
        StringBuilder sb = new StringBuilder();
        long constructed = counterGroup.getTaskStateValue(TaskState.CONSTRUCTED);
        long processed = counterGroup.getTaskStateValue(TaskState.PROCESSED);
        sb.append(ReportingConf.ANSI_BLUE).append("[").append(ReportingConf.ANSI_GREEN);
        sb.append(constructed).append("/").append(processed);
        sb.append(ReportingConf.ANSI_BLUE).append("]");

        counterGroup.checkInitialized();

        String ctrStr = sb.toString();
        String prefix = ReportingConf.ANSI_GREEN + StringUtils.leftPad(counterGroup.getName(), counterGroup.getName().length());
        return prefix + StringUtils.leftPad(ctrStr, WIDTH - prefix.length());
    }


    @Override
    public void run() {
        doIt();
    }

    public void doIt() {
        while (refresh()) {
            try {
                Thread.sleep(refreshInterval);
//                System.out.print(".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        refresh();
    }

}
