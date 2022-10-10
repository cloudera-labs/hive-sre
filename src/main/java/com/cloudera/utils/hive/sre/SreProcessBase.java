/*
 * Copyright (c) 2022. Cloudera, Inc. All Rights Reserved
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

package com.cloudera.utils.hive.sre;

import com.cloudera.utils.hive.config.Metastore;
import com.cloudera.utils.hive.config.QueryDefinitions;
import com.cloudera.utils.hive.config.SreProcessesConfig;
import com.cloudera.utils.hive.reporting.CounterGroup;
import com.cloudera.utils.sql.QueryDefinition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.concurrent.Callable;

@JsonIgnoreProperties({"parent", "config", "queryDefinitions", "dbsOverride", "includeRegEx", "excludeRegEx", "dbType",
        "outputDirectory", "success", "error", "counterGroup", "testSQL"})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DbSetProcess.class, name = "dbSet"),
        @JsonSubTypes.Type(value = MetastoreQueryProcess.class, name = "metastore.query"),
        @JsonSubTypes.Type(value = MetastoreReportProcess.class, name = "metastore.report")})
public abstract class SreProcessBase implements Callable<String> {
    private String displayName = "not set";
    private String title = null;
    private String note = null;
    private String id = null;
    private Boolean skip = Boolean.FALSE;
    private Boolean active = Boolean.TRUE;
    protected Boolean initializing = Boolean.TRUE;

    private Metastore.DB_TYPE dbType = Metastore.DB_TYPE.MYSQL;

    private ProcessContainer parent;

    // Build after construction
    private QueryDefinitions queryDefinitions = null;
    private String queryDefinitionReference = null;

    private String[] dbsOverride = {};
    private String includeRegEx = null;
    private String excludeRegEx = null;
    private String header = null;
    private String errorDescription = null;
    private String successDescription = null;
    private String errorFilename = null;
    private String successFilename = null;

    protected CounterGroup counterGroup;

    /**
     * allows stdout to be captured if necessary
     */
    public PrintStream success = System.out;
    /**
     * allows stderr to be captured if necessary
     */
    public PrintStream error = System.err;

    // Set during init.
    private String outputDirectory = null;

    // Set after construction.
    private SreProcessesConfig config = null;

    private Boolean testSQL = Boolean.FALSE;

    public String getDisplayName() {
        return "\"" + displayName + "\"";
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Boolean isInitializing() {
        return initializing;
    }

    public void setInitializing(Boolean initializing) {
        this.initializing = initializing;
    }

    public String getUniqueName() {
        if (id != null) {
            return id + ":" + displayName;
        } else {
            return displayName;
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isSkip() {
        return skip;
    }

    public void setSkip(Boolean skip) {
        this.skip = skip;
    }

    public Metastore.DB_TYPE getDbType() {
        return dbType;
    }

    public void setDbType(Metastore.DB_TYPE dbType) {
        this.dbType = dbType;
    }

    public ProcessContainer getParent() {
        return parent;
    }

    public void setParent(ProcessContainer parent) {
        this.parent = parent;
    }

    public String getQueryDefinitionReference() {
        return queryDefinitionReference;
    }

    public void setQueryDefinitionReference(String queryDefinitionReference) {
        this.queryDefinitionReference = queryDefinitionReference;
    }

    protected QueryDefinitions getQueryDefinitions() {
        return queryDefinitions;
    }

    protected void setQueryDefinitions(QueryDefinitions queryDefinitions) {
        this.queryDefinitions = queryDefinitions;
    }

    public void setConfig(SreProcessesConfig config) {
        this.config = config;
    }

    public SreProcessesConfig getConfig() {
        return getParent().getConfig();
    }

    public String[] getDbsOverride() {
        return dbsOverride;
    }

    public void setDbsOverride(String[] dbsOverride) {
        this.dbsOverride = dbsOverride;
        this.includeRegEx = null;
        this.excludeRegEx = null;
    }

    public String getIncludeRegEx() {
        return includeRegEx;
    }

    public void setIncludeRegEx(String includeRegEx) {
        this.includeRegEx = includeRegEx;
        this.dbsOverride = null;
        this.excludeRegEx = null;
    }

    public String getExcludeRegEx() {
        return excludeRegEx;
    }

    public void setExcludeRegEx(String excludeRegEx) {
        this.excludeRegEx = excludeRegEx;
        this.dbsOverride = null;
        this.includeRegEx = null;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getSuccessDescription() {
        return successDescription;
    }

    public void setSuccessDescription(String successDescription) {
        this.successDescription = successDescription;
    }

    public String getErrorFilename() {
        return errorFilename;
    }

    public void setErrorFilename(String errorFilename) {
        this.errorFilename = errorFilename;
    }

    public String getSuccessFilename() {
        return successFilename;
    }

    public void setSuccessFilename(String successFilename) {
        this.successFilename = successFilename;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) throws FileNotFoundException {
        this.outputDirectory = outputDirectory;
        if (getErrorFilename() != null) {
            error = outputFile(outputDirectory + System.getProperty("file.separator") + this.getErrorFilename());
        }
        if (getSuccessFilename() != null) {
            success = outputFile(outputDirectory + System.getProperty("file.separator") + this.getSuccessFilename());
        }
    }

    public Boolean isTestSQL() {
        return testSQL;
    }

    public void setTestSQL(Boolean testSQL) {
        this.testSQL = testSQL;
    }

    protected PrintStream outputFile(String name) throws FileNotFoundException {
        return new PrintStream(new BufferedOutputStream(new FileOutputStream(name)), true);
    }

//    protected Map<String, Parameter> getQueryOverride(String definitionName) {
//        Map<String, Parameter> rtn = null;
//        rtn = getParent().getConfig().getQuery(definitionName);
//        rtn = getParent().getConfig().getQuery(definitionName);
//        return rtn;
//    }
//
    public String getOutputDetails() {
        StringBuilder sb = new StringBuilder();
        if (getSuccessFilename() != null) {
            sb.append("\t" + getSuccessDescription() + " -> " + getOutputDirectory() + System.getProperty("file.separator") +
                    getSuccessFilename() + "\n");
        }
        if (getErrorFilename() != null) {
            sb.append("\t" + getErrorDescription() + " -> " + getOutputDirectory() + System.getProperty("file.separator") +
                    getErrorFilename());
        }
        return sb.toString();
    }

    public void init(ProcessContainer parent) throws FileNotFoundException {
        setParent(parent);
        setInitializing(Boolean.TRUE);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String dbQueryDefReference = "/" + dbType.toString() + this.queryDefinitionReference;
            try {
                URL configURL = this.getClass().getResource(dbQueryDefReference);
                if (configURL == null) {
                    throw new RuntimeException("Can't build URL for Resource: " +
                            dbQueryDefReference);
                }
                String yamlConfigDefinition = IOUtils.toString(configURL);
                setQueryDefinitions(mapper.readerFor(QueryDefinitions.class).readValue(yamlConfigDefinition));
            } catch (Exception e) {
                throw new RuntimeException("Missing resource file: " +
                        dbQueryDefReference, e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Issue getting configs", e);
        }
    }

    public abstract Boolean testSQLScript();

}
