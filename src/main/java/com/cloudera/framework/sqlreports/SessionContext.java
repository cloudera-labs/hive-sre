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
import com.cloudera.framework.sqlreports.navigation.Option;
import com.cloudera.framework.sqlreports.navigation.NavigationSelection;
import com.cloudera.framework.sqlreports.navigation.Scenario;
import com.cloudera.utils.hive.dba.QueryStore;
import com.cloudera.utils.hive.sre.ConnectionPools;

/*
Use to store session specific details that can be referenced
globally by the application.
 */
public class SessionContext {
    private static SessionContext instance = new SessionContext();

    private ConnectionPools connectionPools;
    private QueryStore queryStore;
    private QueryResource queryResource;
    private NavigationTree navigationTree;

    private NavState state = new NavState();

    public static SessionContext getInstance() {
        return instance;
    }

    private SessionContext() {

    }

    public ConnectionPools getConnectionPools() {
        return connectionPools;
    }

    public void setConnectionPools(ConnectionPools connectionPools) {
        this.connectionPools = connectionPools;
    }

    public QueryStore getQueryStore() {
        return queryStore;
    }

    public void setQueryStore(QueryStore queryStore) {
        this.queryStore = queryStore;
    }

    public QueryResource getQueryResource() {
        return queryResource;
    }

    public void setQueryResource(QueryResource queryResource) {
        this.queryResource = queryResource;
    }

    public NavigationTree getNavigationTree() {
        return navigationTree;
    }

    public void setNavigationTree(NavigationTree navigationTree) {
        this.navigationTree = navigationTree;
    }

    public NavState getState() {
        return state;
    }

    /*

     */
    public NavigationSelection push(String referenceStr) {
        // Get the current selection
        NavigationSelection currentSelection = peek();
        // TODO: Account for non selection values..
        Integer reference = Integer.parseInt(referenceStr);
        int iLoop = 0;
        if (currentSelection == null) {
            // Top of the NavTree (root)
            for (String groupKey: getNavigationTree().getGroupings().keySet()) {
                iLoop++;
                if (iLoop == reference) {
                    currentSelection = getNavigationTree().getGroupings().get(groupKey);
//                    getState().getSelectionQueue().add(currentSelection);
                    getState().addPath(currentSelection);
                    return currentSelection;
                }
            }
        } else {
            if (currentSelection instanceof Group &&
                    ((Group)currentSelection).getOptions() != null &&
                    ((Group)currentSelection).getOptions().size() > 0) {
                for (Option option: ((Group)currentSelection).getOptions()) {
                    iLoop++;
                    if (reference == iLoop) {
//                        getState().getSelectionQueue().add(option);
                        getState().addPath(option);
                        return option;
                    }
                }
            }
            if (currentSelection instanceof Option &&
                    ((Option)currentSelection).getScenarios() != null &&
                    ((Option)currentSelection).getScenarios().size() > 0) {
                for (Scenario scenario: ((Option)currentSelection).getScenarios()) {
                    iLoop++;
                    if (reference == iLoop) {
//                        getState().getSelectionQueue().add(scenario);
                        getState().addPath(scenario);
                        return scenario;
                    }
                }
            }
        }
        return currentSelection;
    }

    /*
        LIFO
         */
    public NavigationSelection pop() {
        return getState().getSelectionQueue().pollLast();
    }

    /*
    LIFO
     */
    public NavigationSelection peek() {
        return getState().getSelectionQueue().peekLast();
    }
}
