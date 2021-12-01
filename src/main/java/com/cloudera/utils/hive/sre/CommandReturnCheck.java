/*
 * Copyright 2021 Cloudera, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.utils.hive.sre;

import com.cloudera.utils.hive.reporting.ReportCounter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.cloudera.utils.hadoop.shell.command.CommandReturn;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.PrintStream;
import java.util.*;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY,
//        property = "type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = PathCheck.class, name = "path.check"),
//        @JsonSubTypes.Type(value = SmallFiles.class, name = "small.files"),
//        @JsonSubTypes.Type(value = FilenameFormatCheck.class, name = "filename.format"),
//        @JsonSubTypes.Type(value = DirectoryExistsCheck.class, name = "directory.exists")
//})
@JsonIgnoreProperties({"counter", "properties", "calculationResults", "scriptEngine"})
public class CommandReturnCheck {

    private String displayName;
    private String title;
    private String header;
    private String note;
    private String pathCommand;

    private String errorDescription = null;
    private String successDescription = null;
    private String errorFilename = null;
    private String successFilename = null;

    // Most commands that run will not be an error, but are issues that need to
    // be put into the 'error' or action bucket.  Use this to control that direction.
    private Boolean invertCheck = true;
    // Determine if the on..Commands should be run against the "path" or
    //      each record in the CommandReturn.
    private Boolean reportOnResults = Boolean.FALSE;
    private Boolean reportOnPath = Boolean.TRUE;
    private Boolean processOnError = Boolean.TRUE;
    private Boolean processOnSuccess = Boolean.TRUE;
    private String onSuccessRecordCommand;
    private String onErrorRecordCommand;
    private String onSuccessPathCommand;
    private String onErrorPathCommand;
    private Map<String, Map<CheckSearch, CheckCalculation>> checkCalculations = null;
    private ScriptEngine scriptEngine = null;
    private Map<String, Object> calculationResults = null;
//    private String[] currentArgs;
    /**
     * allows stdout to be captured if necessary
     */
    public PrintStream successStream = System.out;
    /**
     * allows stderr to be captured if necessary
     */
    public PrintStream errorStream = System.err;

    public ReportCounter counter = null;//new ReportCounter();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public void onError(CommandReturn commandReturn, String[] args) {
        if (!invertCheck) {
            internalOnError(commandReturn, args);
        } else {
            internalOnSuccess(commandReturn, args);
        }
    }

    public void onSuccess(CommandReturn commandReturn, String[] args) {
        if (!invertCheck) {
            internalOnSuccess(commandReturn, args);
        } else {
            internalOnError(commandReturn, args);
        }
    }

    public String runCalculations(CommandReturn commandReturn, String[] args) {
        String rtn = null;
        try {
            if (getCheckCalculations() != null && getCheckCalculations().size() > 0 && getScriptEngine() != null) {
                StringBuilder sb = new StringBuilder();
                for (String calcKey : checkCalculations.keySet()) {
                    Map<CheckSearch, CheckCalculation> checkSearchCalculation = checkCalculations.get(calcKey);
                    for (CheckSearch checkSearch : checkSearchCalculation.keySet()) {
                        CheckCalculation checkCalculation = checkSearchCalculation.get(checkSearch);
                        String testStr = null;
                        String failStr = null;
                        String passStr = null;
                        switch (checkSearch) {
                            case PATH:
                                if (checkCalculation.getTest() != null) {
                                    // Params
                                    List combined = new LinkedList(Arrays.asList(args));
                                    // Configured Params
                                    if (checkCalculation.getParams() != null)
                                        combined.addAll(Arrays.asList(checkCalculation.getParams()));
                                    try {
                                        testStr = String.format(checkCalculation.getTest(), combined.toArray());
                                        Boolean checkTest = null;
                                        checkTest = (Boolean) scriptEngine.eval(testStr);
                                        if (checkTest) {
                                            if (checkCalculation.getPass() != null) {
                                                passStr = String.format(checkCalculation.getPass(), combined.toArray());
                                                String passResult = (String) scriptEngine.eval(passStr);
                                                sb.append(passResult).append("\n");
                                            }

                                        } else {
                                            if (checkCalculation.getFail() != null) {
                                                failStr = String.format(checkCalculation.getFail(), combined.toArray());
                                                String failResult = (String) scriptEngine.eval(failStr);
                                                sb.append(failResult).append("\n");
                                            }
                                        }
                                    } catch (ScriptException e) {
                                        e.printStackTrace();
                                        System.err.println("Issue with script eval: " + this.getDisplayName() + ":" + calcKey);
                                    } catch (MissingFormatArgumentException mfa) {
                                        mfa.printStackTrace();
                                        System.err.println("Bad Argument Match up for PATH check rule: " + this.getDisplayName() + ":" + calcKey);
                                    }
                                }
                                break;
                            case RECORDS:
                                // Loop Through Records.
                                if (checkCalculation.getTest() != null) {
                                    for (List<Object> record : commandReturn.getRecords()) {
                                        // Params
                                        List combined = new LinkedList(Arrays.asList(args));
                                        // Current Record
                                        combined.addAll(record);
                                        // Configured Params
                                        if (checkCalculation.getParams() != null)
                                            combined.addAll(Arrays.asList(checkCalculation.getParams()));
                                        try {
                                            testStr = String.format(checkCalculation.getTest(), combined.toArray());
                                            Boolean checkTest = null;
                                            checkTest = (Boolean) scriptEngine.eval(testStr);
                                            if (checkTest) {
                                                if (checkCalculation.getPass() != null) {
                                                    passStr = String.format(checkCalculation.getPass(), combined.toArray());
                                                    String passResult = (String) scriptEngine.eval(passStr);
                                                    sb.append(passResult).append("\n");
                                                }

                                            } else {
                                                if (checkCalculation.getFail() != null) {
                                                    failStr = String.format(checkCalculation.getFail(), combined.toArray());
                                                    String failResult = (String) scriptEngine.eval(failStr);
                                                    sb.append(failResult).append("\n");
                                                }
                                            }
                                        } catch (ScriptException e) {
                                            e.printStackTrace();
                                            System.err.println("Issue with script eval: " + this.getDisplayName() + ":" + calcKey);
                                        } catch (MissingFormatArgumentException mfa) {
                                            mfa.printStackTrace();
                                            System.err.println("Bad Argument Match up for RECORDS check rule: " + this.getDisplayName() + ":" + calcKey);
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
                rtn = sb.toString();
            }
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return rtn;
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    private void internalOnError(CommandReturn commandReturn, String[] args) {
        StringBuilder sb = new StringBuilder();
        if (getReportOnPath() && getOnErrorPathCommand() != null) {
            String action = null;
            try {
                action = String.format(getOnErrorPathCommand(), args);
            } catch (Throwable t) {
                throw new RuntimeException("Bad string format in 'errorPath' action command of CommandReturnCheck", t);
            }
            if (action != null)
                sb.append(action).append("\n");
        }

        if (getReportOnResults() && getOnErrorRecordCommand() != null) {
            for (List<Object> record : commandReturn.getRecords()) {
                String action = null;
                String[] combined = new String[args.length + record.size()];
                System.arraycopy(args, 0, combined, 0, args.length);
                System.arraycopy(record.toArray(), 0, combined, args.length, record.toArray().length);
                try {
                    action = String.format(getOnErrorRecordCommand(), combined);
                } catch (Throwable t) {
                    throw new RuntimeException("Bad string format in 'errorRecord' action command of CommandReturnCheck", t);
                }
                if (action != null)
                    sb.append(action).append("\n");
            }
        }
        if (getProcessOnError()) {
            String checkCalcs = runCalculations(commandReturn, args);
            if (checkCalcs != null)
                sb.append(checkCalcs);
        }
        errorStream.print(sb.toString());
    }

    private void internalOnSuccess(CommandReturn commandReturn, String[] args) {
        StringBuilder sb = new StringBuilder();
        if (getReportOnPath() && getOnSuccessPathCommand() != null) {
            String action = null;
            try {
                action = String.format(getOnSuccessPathCommand(), args);
            } catch (Throwable t) {
                throw new RuntimeException("Bad string format in 'successPath' action command of CommandReturnCheck", t);
            }
            if (action != null)
                sb.append(action).append("\n");
        }
        if (getReportOnResults() && getOnSuccessRecordCommand() != null) {
            for (List<Object> record : commandReturn.getRecords()) {
                Object[] rec = new Object[record.size()];
                for (int i = 0; i < record.size(); i++) {
                    rec[i] = record.get(i);
                }
                String action = null;
                try {
                    action = String.format(getOnSuccessRecordCommand(), rec);
                } catch (Throwable t) {
                    throw new RuntimeException("Bad string format in 'successRecord' action command of CommandReturnCheck", t);
                }
                if (action != null)
                    sb.append(action).append("\n");
            }
        }
        if (getProcessOnSuccess()) {
            String checkCalcs = runCalculations(commandReturn, args);
            if (checkCalcs != null)
                sb.append(checkCalcs);
        }
        successStream.print(sb.toString());
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        this.counter = new ReportCounter(this.displayName);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getInvertCheck() {
        return invertCheck;
    }

    public void setInvertCheck(Boolean invertCheck) {
        this.invertCheck = invertCheck;
    }

    public String getPathCommand() {
        return pathCommand;
    }

    public void setPathCommand(String pathCommand) {
        this.pathCommand = pathCommand;
    }

    public String getOnSuccessRecordCommand() {
        return onSuccessRecordCommand;
    }

    public void setOnSuccessRecordCommand(String onSuccessRecordCommand) {
        this.onSuccessRecordCommand = onSuccessRecordCommand;
    }

    public String getOnErrorRecordCommand() {
        return onErrorRecordCommand;
    }

    public void setOnErrorRecordCommand(String onErrorRecordCommand) {
        this.onErrorRecordCommand = onErrorRecordCommand;
    }

    public Boolean getReportOnResults() {
        return reportOnResults;
    }

    public void setReportOnResults(Boolean reportOnResults) {
        this.reportOnResults = reportOnResults;
    }

    public Boolean getReportOnPath() {
        return reportOnPath;
    }

    public void setReportOnPath(Boolean reportOnPath) {
        this.reportOnPath = reportOnPath;
    }

    public Boolean getProcessOnError() {
        return processOnError;
    }

    public void setProcessOnError(Boolean processOnError) {
        this.processOnError = processOnError;
    }

    public Boolean getProcessOnSuccess() {
        return processOnSuccess;
    }

    public void setProcessOnSuccess(Boolean processOnSuccess) {
        this.processOnSuccess = processOnSuccess;
    }

    public String getOnSuccessPathCommand() {
        return onSuccessPathCommand;
    }

    public void setOnSuccessPathCommand(String onSuccessPathCommand) {
        this.onSuccessPathCommand = onSuccessPathCommand;
    }

    public String getOnErrorPathCommand() {
        return onErrorPathCommand;
    }

    public void setOnErrorPathCommand(String onErrorPathCommand) {
        this.onErrorPathCommand = onErrorPathCommand;
    }

    public Map<String, Map<CheckSearch, CheckCalculation>> getCheckCalculations() {
        return checkCalculations;
    }

    public void setCheckCalculations(Map<String, Map<CheckSearch, CheckCalculation>> checkCalculations) {
        if (checkCalculations != null && checkCalculations.size() > 0) {
            ScriptEngineManager sem = new ScriptEngineManager();
            scriptEngine = sem.getEngineByName("nashorn");
        }
        this.checkCalculations = checkCalculations;

    }

    public String getFullCommand(String[] args) {
//        setCurrentArgs(args);
        String action = String.format(getPathCommand(), args);
        return action;
    }

    public ReportCounter getCounter() {
        return counter;
    }

    public void setCounter(ReportCounter counter) {
        this.counter = counter;
    }

//    @Override
//    public int getStatus() {
//        return counter.getStatus();
//    }
//
//    @Override
//    public String getStatusStr() {
//        return counter.getStatusStr();
//    }
//
//    @Override
//    public List<ReportCounter> getCounterChildren() {
//        return counter.getChildren();
//    }
//
//    @Override
//    public void setStatus(int status) {
//        counter.setStatus(status);
//    }
//
//    @Override
//    public void incProcessed(int increment) {
//        counter.incProcessed(increment);
//    }
//
//    @Override
//    public void setTotalCount(long totalCount) {
//        counter.setConstructedCount(totalCount);
//    }
//
//    @Override
//    public void incSuccess(int increment) {
//        counter.incSuccess(increment);
//    }
//
//    @Override
//    public void incError(int increment) {
//        counter.incError(increment);
//    }

//    public String[] getCurrentArgs() {
//        return currentArgs;
//    }
//
//    public void setCurrentArgs(String[] currentArgs) {
//        this.currentArgs = new String[currentArgs.length];
//        System.arraycopy(currentArgs, 0, this.currentArgs, 0, currentArgs.length);
//    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        CommandReturnCheck clone = (CommandReturnCheck) super.clone();
        clone.setDisplayName(this.displayName);
        clone.setTitle(this.title);
        clone.setOnErrorRecordCommand(this.onErrorRecordCommand);
        clone.setOnSuccessRecordCommand(this.onSuccessRecordCommand);
        clone.setReportOnPath(this.reportOnPath);
        clone.setReportOnResults(this.reportOnResults);
        clone.setProcessOnError(this.processOnError);
        clone.setProcessOnSuccess(this.processOnSuccess);
//        clone.setCounter(new ReportCounter());
        clone.getCounter().setName(this.displayName);
        clone.setCheckCalculations(this.checkCalculations);
        clone.setErrorStream(this.errorStream);
        clone.setErrorDescription(this.errorDescription);
        clone.setErrorFilename(this.errorFilename);
        clone.setHeader(this.header);
        clone.setSuccessStream(this.successStream);
        clone.setSuccessDescription(this.successDescription);
        clone.setSuccessFilename(this.successFilename);
        return clone;

    }

}
