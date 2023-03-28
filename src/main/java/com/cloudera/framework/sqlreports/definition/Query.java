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

package com.cloudera.framework.sqlreports.definition;

import com.cloudera.framework.sqlreports.SessionContext;
import com.cloudera.utils.hive.config.QueryDefinitions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.io.IOUtils;
//import org.apache.curator.shaded.com.google.common.io.Files;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Query implements Cloneable {

    /*
    The queries resource reference.  This should be a resource 'file' reference that contains the SQL
    statement to run.

    This separation will allow us to easily test the sql independently without having to do a bunch of
    escaping in an embedded yaml file.
     */
    private String resource = null;

    @JsonIgnore
    private List<String> query = null;

    @JsonProperty(value = "default_parameters")
    private Map<String, Object> parameters = new HashMap<String, Object>();

    // TODO: Need to complete for Cloneable
    @JsonProperty(value = "field_translations")
    private Map<String, Translation> translations = new HashMap<String, Translation>();

    @JsonProperty(value = "detail_references")
    private List<String> detailReferences = null;
    @JsonProperty(value = "detail_groups")
    private List<String> detailGroups = null;

    /*
    The resolved (cloned) Item resource used for detail drilldowns.
     */
    @JsonIgnore
    private List<Item> details = new ArrayList<Item>();

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public List<String> getQuery() {
        if (query == null || query.size() == 0) {
            // Resolve the query from the resources.  Use the session context to set the
            // path to the resource.
            if (SessionContext.getInstance().getQueryStore() == null) {
                throw new RuntimeException("Query Store `query_analysis` hasn't been defined in the configuration yaml.  Can't establish proper resource path.");
            }

            String queryResource = "/dba/" + SessionContext.getInstance().getQueryStore().getType() +
                    "/" + getResource();

            try {
                URL queryResourceURL = this.getClass().getResource(queryResource);
                if (queryResourceURL == null) {
                    throw new RuntimeException("Can't build URL for Resource: " +
                            queryResource);
                }
                query = IOUtils.readLines(queryResourceURL.openStream(), Charset.forName("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException("Missing resource file: " +
                        queryResource, e);
            }
        }
        return query;
    }

    public void setQuery(List<String> query) {
        this.query = query;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(Map<String, Translation> translations) {
        this.translations = translations;
    }

    public List<String> getDetailReferences() {
        return detailReferences;
    }

    public void setDetailReferences(List<String> detailReferences) {
        this.detailReferences = detailReferences;
    }

    public List<String> getDetailGroups() {
        return detailGroups;
    }

    public void setDetailGroups(List<String> detailGroups) {
        this.detailGroups = detailGroups;
    }

    public Boolean hasDetails() {
        if (details != null && details.size() > 0)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    public List<Item> getDetails() {
        return details;
    }

    public void setDetails(List<Item> details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Query query1 = (Query) o;

        if (!Objects.equals(resource, query1.resource)) return false;
        if (!Objects.equals(query, query1.query)) return false;
        if (!Objects.equals(parameters, query1.parameters)) return false;
        return Objects.equals(translations, query1.translations);
    }

    @Override
    public int hashCode() {
        int result = resource != null ? resource.hashCode() : 0;
        result = 31 * result + (query != null ? query.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (translations != null ? translations.hashCode() : 0);
        return result;
    }

    @Override
    public Query clone() throws CloneNotSupportedException {
        Query clonedQuery = (Query) super.clone();
//        clonedQuery.setResource(this.resource);
        if (this.query != null)
            clonedQuery.setQuery(new ArrayList<String>(this.query));
        if (this.getParameters() != null)
            clonedQuery.setParameters(new HashMap<String, Object>(this.parameters));
        return clonedQuery;
    }

}
