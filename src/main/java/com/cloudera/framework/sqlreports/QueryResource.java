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
import com.cloudera.framework.sqlreports.definition.Query;
import com.cloudera.framework.sqlreports.definition.QueryGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class QueryResource {
    private static final Logger LOG = LogManager.getLogger(QueryResource.class);

    private Map<String, Item> definitions = new HashMap<String, Item>();

    private Map<String, QueryGroup> groups = new HashMap<String, QueryGroup>();

    @JsonProperty
    private Boolean resolvedReferences = Boolean.FALSE;

    public Map<String, Item> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Map<String, Item> definitions) {
        this.definitions = definitions;
    }

    public Map<String, QueryGroup> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, QueryGroup> groups) {
        this.groups = groups;
    }

   protected Boolean resolveAndAddDefinition(Query query, String definitionReference) {
       Boolean rtn = Boolean.TRUE;
       Item referencedItem = definitions.get(definitionReference);
       if (referencedItem != null) {
           Item clonedReferenceItem = null;
           try {
               clonedReferenceItem = referencedItem.clone();
           } catch (CloneNotSupportedException e) {
               throw new RuntimeException(e);
           }
           query.getDetails().add(clonedReferenceItem);
       } else {
           LOG.error("Definition: " + definitionReference + " not found.  Fix definition.");
           rtn = Boolean.FALSE;
       }
        return rtn;
   }

    /*
        There are object in the instance that are references to other objects.  This is designed to create
        copies of those references and associate them where needed.

        Should be called BEFORE loadResources.
         */
    protected Boolean resolveReferences() {
        Boolean rtn = Boolean.TRUE;
        if (!resolvedReferences) {
            for (String defName : getDefinitions().keySet()) {
                Item item = definitions.get(defName);
                Query query = item.getQuery();
                // If there are references AND they haven't already been loaded.
                if (query.getDetailReferences() != null && query.getDetails().size() == 0) {
                    for (String itemReference : query.getDetailReferences()) {
                        Boolean addCheck = resolveAndAddDefinition(query, itemReference);
                        if (!addCheck)
                            rtn = addCheck;
                    }
                }
                if (query.getDetailGroups() != null) {
                    for (String detailGroupKey : query.getDetailGroups()) {
                        // Find this group in the queryresource.
                        QueryGroup queryGroup = groups.get(detailGroupKey);
                        for (String itemReference : queryGroup.getDetailReferences()) {
                            Boolean addCheck = resolveAndAddDefinition(query, itemReference);
                            if (!addCheck)
                                rtn = addCheck;
                        }
                    }
                }
            }
            resolvedReferences = Boolean.TRUE;
        }
        return rtn;
    }

    public Boolean loadResources() {
        // Need to Resolve first.
        if (!resolveReferences())
            return Boolean.FALSE;
        Boolean rtn = Boolean.TRUE;
        // This should resolve just the first level and 'details' of the first level.
        // Even if defined, it shouldn't get recursive.
        for (String defName: getDefinitions().keySet()) {
            Item item = definitions.get(defName);
            try {
                // This will trigger the queries to resolve from the resource link.
                Query query = item.getQuery();
                query.getQuery();
                // Resolve the 'detail' resolution
                if (query.getDetails().size() > 0) {
                    for (Item qItem: query.getDetails()) {
                        qItem.getQuery().getQuery();
                    }
                }
            } catch (RuntimeException rte) {
                // Log error.
                rte.printStackTrace();
                rtn = Boolean.FALSE;
                LOG.error("Resource: "+ defName, rte);
            }
        }
        return rtn;
    }
}
