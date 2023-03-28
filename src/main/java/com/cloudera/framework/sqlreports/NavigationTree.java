/*
 * Copyright (c) 2022-2023. Cloudera, Inc. All Rights Reserved
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

import com.cloudera.framework.sqlreports.definition.Item;
import com.cloudera.framework.sqlreports.navigation.Group;
import com.cloudera.framework.sqlreports.navigation.Option;
import com.cloudera.framework.sqlreports.navigation.Score;
import com.cloudera.framework.sqlreports.navigation.WeightedOption;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class NavigationTree {
    private static final Logger LOG = LogManager.getLogger(NavigationTree.class);
    private Map<String, Group> groupings = new HashMap<String, Group>();
    public Map<String, Score> scorings = new HashMap<String, Score>();

    public Map<String, Group> getGroupings() {
        return groupings;
    }

    public void setGroupings(Map<String, Group> groupings) {
        this.groupings = groupings;
    }

    public Map<String, Score> getScorings() {
        return scorings;
    }

    public void setScorings(Map<String, Score> scorings) {
        this.scorings = scorings;
    }

    public Boolean loadResources() {
        Boolean rtn = Boolean.TRUE;
        for (String key : groupings.keySet()) {
            Group group = groupings.get(key);
            group.loadResources();
        }
        for (String key: scorings.keySet()) {
            Score score = scorings.get(key);
            score.loadResources();
        }
        return rtn;
    }

    /*
    TODO: Complete 'showTOC()'
    The goal of this output is to provide a map (with descriptions) of the Navigation Tree
    so people can easily get a list guide of this application.
     */
    public String showTOC() {
        StringBuilder sb = new StringBuilder();
        sb.append("Groups").append("\n");
        for (String groupKey: groupings.keySet()) {
            Group group = groupings.get(groupKey);
            showGroupTOC(1, sb, group);
        }
        sb.append("Scores").append("\n");
        for (String scoreKey: scorings.keySet()) {
            Score score = scorings.get(scoreKey);
            showScoreTOC(1, sb, score);
        }
        return sb.toString();
    }

    private final String INDENT = "\t";

    private String indent(int level) {
        StringBuilder isb = new StringBuilder();
        for (int i = 0;i<level;i++) {
            isb.append(INDENT);
        }
        return isb.toString();
    }

    protected void showGroupTOC(int level, StringBuilder sb, Group group) {
        sb.append(indent(level)).append(group.getShortDesc()).append("\n");
        for (Option option: group.getOptions()) {
            sb.append(indent(level+1)).append(option.getShortDesc()).append("\n");
            if (option.getQueryItem().getQuery().hasDetails()) {
                sb.append(indent(level+2)).append("-- Details --").append("\n");
                for (Item iItem: option.getQueryItem().getQuery().getDetails()) {
                    sb.append(indent(level+2)).append(iItem.getShortDesc()).append("\n");
                }
            }
            if (option.hasDrillDowns()) {
                sb.append(indent(level+2)).append("-- Drill-downs --").append("\n");
                for (Option ddOption: option.getDrillDowns()) {
                    sb.append(indent(level+3)).append(ddOption.getShortDesc()).append("\n");
                    if (ddOption.getQueryItem().getQuery().hasDetails()) {
                        sb.append(indent(level+3)).append("-- Details --").append("\n");
                        for (Item iItem: ddOption.getQueryItem().getQuery().getDetails()) {
                            sb.append(indent(level+4)).append(iItem.getShortDesc()).append("\n");
                        }
                    }
                }
            }
        }
        if (!group.getGroupings().isEmpty()) {
            for (String subGroupKey: group.getGroupings().keySet()) {
                Group subGroup = group.getGroupings().get(subGroupKey);
                showGroupTOC(level + 1, sb, subGroup);
            }
        }
    }

    protected void showScoreTOC(int level, StringBuilder sb, Score score) {
        sb.append(indent(level)).append(score.getShortDesc()).append("\n");
    }

}
