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

public class DrillDown {
    @JsonProperty(value = "definition_reference")
    private String definitionReference = null;

    @JsonIgnore
    private Item queryItem = null;

    // TODO: Field Mappings

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
}
