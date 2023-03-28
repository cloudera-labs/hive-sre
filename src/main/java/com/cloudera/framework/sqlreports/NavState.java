/*
 * Copyright (c) 2023. Cloudera, Inc. All Rights Reserved
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

package com.cloudera.framework.sqlreports;

import com.cloudera.framework.sqlreports.navigation.Group;
import com.cloudera.framework.sqlreports.navigation.NavigationSelection;
import com.cloudera.framework.sqlreports.navigation.Option;
import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.sql.ResultArray;

import java.util.*;
import java.util.stream.Collectors;

public class NavState {
    private Deque<NavigationSelection> selectionQueue = new ArrayDeque<NavigationSelection>();

    /*
    Contain the results from an option...
    Needs to be cleared when using another option or scenario.
     */
    private ResultArray results = null;
    /*
    Use to record the selection from the result of an Option scenario
     */
    private Map<String, Object> selection = new HashMap<String, Object>();

    public Deque<NavigationSelection> getSelectionQueue() {
        return selectionQueue;
    }

    /*
        When the group queue is empty, where at the root of the NavTree.
         */
    public Map<String, Object> getSelection() {
        return selection;
    }

    public ResultArray getResults() {
        return results;
    }

    public void setResults(ResultArray results) {
        this.results = results;
    }

    public String getPath() {
        return getSelectionQueue().
                stream().map(nav -> nav.getName()).collect(Collectors.joining("->"));
    }

    public void addPath(NavigationSelection navigationSelection) {
        getSelectionQueue().add(navigationSelection);
//        System.out.println(ReportingConf.CLEAR_CONSOLE);
//        System.out.println("Current path:");
//        System.out.println("\t" + getPath());
    }
}
