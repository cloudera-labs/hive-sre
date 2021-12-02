package com.cloudera.utils.hive.sre;

import com.cloudera.utils.hive.reporting.ReportCounter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.script.ScriptEngine;
import java.io.PrintStream;
import java.util.Map;

@JsonIgnoreProperties({"counter", "scriptEngine"})
public class SkipCommandCheck {

    private String displayName;
    private String title;
    private String note;
    private String record;

    private ScriptEngine scriptEngine = null;
    /**
     * allows stdout to be captured if necessary
     */
    public PrintStream successStream = System.out;
    /**
     * allows stderr to be captured if necessary
     */
    public PrintStream errorStream = System.err;

    public ReportCounter counter = null;//new ReportCounter();

    public String getDisplayName() {
        return displayName;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public void setScriptEngine(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    public PrintStream getSuccessStream() {
        return successStream;
    }

    public void setSuccessStream(PrintStream successStream) {
        this.successStream = successStream;
    }

    public PrintStream getErrorStream() {
        return errorStream;
    }

    public void setErrorStream(PrintStream errorStream) {
        this.errorStream = errorStream;
    }

    public ReportCounter getCounter() {
        return counter;
    }

    public void setCounter(ReportCounter counter) {
        this.counter = counter;
    }
}
