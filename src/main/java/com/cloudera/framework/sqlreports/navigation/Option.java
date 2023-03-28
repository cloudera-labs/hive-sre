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

import com.cloudera.framework.sqlreports.SessionContext;
import com.cloudera.framework.sqlreports.definition.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Option implements NavigationSelection, Description {

    public String getType() {
        return "Options";
    }
    public String getName() {
        return getQueryItem().getShortDesc();
    }

    @JsonProperty(value = "definition_reference")
    private String definitionReference = null;

    @JsonIgnore
    private Item queryItem = null;

    public String getShortDesc() {
        if (queryItem != null)
            return queryItem.getShortDesc();
        else
            return "";
    }

    public String getLongDesc() {
        if (queryItem != null)
            return queryItem.getLongDesc();
        else
            return "";
    }

    private List<Scenario> scenarios = new ArrayList<Scenario>();

    private List<String> display = new ArrayList<String>();

    @JsonProperty(value = "drill_downs")
    private List<Option> drillDowns = new ArrayList<Option>();
    public String getDefinitionReference() {
        return definitionReference;
    }

    public void setDefinitionReference(String definitionReference) {
        this.definitionReference = definitionReference;
    }

    public Item getQueryItem() {
        if (queryItem == null) {
            queryItem = SessionContext.getInstance().getQueryResource().getDefinitions().get(getDefinitionReference());
            if (queryItem == null) {
                throw new RuntimeException("Couldn't locate query definition reference: " + getDefinitionReference());
            }
        }
        return queryItem;
    }

    public void setQueryItem(Item queryItem) {
        this.queryItem = queryItem;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public List<String> getDisplay() {
        return display;
    }

    public void setDisplay(List<String> display) {
        this.display = display;
    }

    public Boolean hasDrillDowns() {
        if (drillDowns != null && drillDowns.size() > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public List<Option> getDrillDowns() {
        return drillDowns;
    }

    public void setDrillDowns(List<Option> drillDowns) {
        this.drillDowns = drillDowns;
    }


}
