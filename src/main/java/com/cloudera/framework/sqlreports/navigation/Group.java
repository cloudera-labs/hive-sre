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

package com.cloudera.framework.sqlreports.navigation;

import com.cloudera.framework.sqlreports.NavigationTree;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group implements NavigationSelection, Description {

    private static final Logger LOG = LogManager.getLogger(Group.class);

    public String getType() {
        return "Groups";
    }
    private String name = null;

    private String longDesc = null;

    private List<Option> options = new ArrayList<Option>();

    // Nesting here?
    private Map<String, Group> groupings = new HashMap<String, Group>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getShortDesc() {
        return name;
    }

    @Override
    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Map<String, Group> getGroupings() {
        return groupings;
    }

    public void setGroupings(Map<String, Group> groupings) {
        this.groupings = groupings;
    }

    public void loadResources() {
        for (Option option : getOptions()) {
            try {
                option.getQueryItem();
                for (Option ddOption: option.getDrillDowns()) {
                    ddOption.getQueryItem();
                }
                for (String groupKey: getGroupings().keySet()) {
                    getGroupings().get(groupKey).loadResources();
                }
            } catch (RuntimeException rte) {
                LOG.error(rte);
                rte.printStackTrace();
            }
        }
    }
}
