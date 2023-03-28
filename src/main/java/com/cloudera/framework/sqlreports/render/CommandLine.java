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

package com.cloudera.framework.sqlreports.render;

import com.cloudera.framework.sqlreports.Execution;
import com.cloudera.framework.sqlreports.NavState;
import com.cloudera.framework.sqlreports.NavigationTree;
import com.cloudera.framework.sqlreports.SessionContext;
import com.cloudera.framework.sqlreports.definition.Item;
import com.cloudera.framework.sqlreports.format.Formatter;
import com.cloudera.framework.sqlreports.format.Markdown;
import com.cloudera.framework.sqlreports.format.Screen;
import com.cloudera.framework.sqlreports.navigation.Option;
import com.cloudera.framework.sqlreports.navigation.*;
import com.cloudera.utils.common.TokenReplacement;
import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.sql.ResultArray;
import org.apache.commons.cli.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CommandLine {
    //    private Map<String, NavigationSelection> selections = new TreeMap<String, NavigationSelection>();
    private static Logger LOG = LogManager.getLogger(CommandLine.class);

    private Execution execution = null;
    private Formatter formatter = new Markdown();
    private Formatter screen = new Screen();
    private Options parentOptions = null;
    private ResultArray currentResults = null;
    private List<String> currentDisplay = null;
    private Scenario lastScenario = null;
    private int screenWidth = 100;

    protected Boolean hasResults() {
        if (currentResults != null && currentResults.getRecords().size() > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private PrintStream output = null;

    private Boolean inited = Boolean.FALSE;

    public CommandLine(Options options) {
        this.parentOptions = options;
        System.out.println(ReportingConf.CLEAR_CONSOLE);
        String myCol = System.getenv("MY_COLUMNS");
        if (myCol != null) {
            try {
                screenWidth = Integer.parseInt(myCol);
            } catch (NumberFormatException nfe) {
                // skip and use default.
                LOG.warn("Couldn't determine screen width, using default.");
            }
        }
    }

    public void clearResults() {
        currentResults = null;
        currentDisplay = null;
        lastScenario = null;
    }

    public Scenario getLastScenario() {
        return lastScenario;
    }

    public void setLastScenario(Scenario lastScenario) {
        this.lastScenario = lastScenario;
    }


    protected Options getOptions() {
        Options subOptions = new Options();
        for (Object optionObject : parentOptions.getOptions()) {
            org.apache.commons.cli.Option option = (org.apache.commons.cli.Option) optionObject;
            subOptions.addOption(option);
        }
        /*
        - Output File = When included will output to System.Out and a file.
        - Report-Only = Run the whole tree and report out on the 'first (1)' scenario.

         */
        org.apache.commons.cli.Option outputOption = new org.apache.commons.cli.Option("o", "output-dir", true,
                "Output Directory to save results from Sre.");
        outputOption.setRequired(false);
        subOptions.addOption(outputOption);

        org.apache.commons.cli.Option roOption = new org.apache.commons.cli.Option("ro", "report-only", false,
                "Run all reports automatically using the first scenario.");
        roOption.setRequired(false);
        subOptions.addOption(roOption);

        return subOptions;
    }

    public void init(String[] args) {
        // Deal with Options.
        CommandLineParser parser = new PosixParser();
        org.apache.commons.cli.CommandLine cmd = null;

        try {
            cmd = parser.parse(getOptions(), args);
        } catch (ParseException pe) {
            HelpFormatter formatter = new HelpFormatter();
            String cmdline = ReportingConf.substituteVariablesFromManifest("hive-sre dba" + "\nversion:${Implementation-Version}");
            formatter.printHelp(100, cmdline, "Hive SRE Utility", getOptions(),
                    "\nVisit https://github.com/cloudera-labs/hive-sre for detailed docs");
            System.err.println(pe.getMessage());
            System.exit(-1);
        }

        String outputDirectory = null;
        if (cmd.hasOption("o")) {
            outputDirectory = cmd.getOptionValue("o");
        } else {
            outputDirectory = "hive-sre-output" + System.getProperty("file.separator") + "dba";
        }

        File jobDir = new File(outputDirectory);
        if (!jobDir.exists()) {
            jobDir.mkdirs();
        }

        DateFormat df = new SimpleDateFormat("YY-MM-dd_HH-mm-ss");

        try {
            File reportFile = new File(outputDirectory + System.getProperty("file.separator") +
                    "hive-sre-dba-" + df.format(new Date()) + ".md");
            output = new PrintStream(new BufferedOutputStream(new FileOutputStream(reportFile)), true);
            output.println(ReportingConf.substituteVariables("v.${App-Version}"));
            output.println(df.format(new Date()) + "\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Problem 'writing' Report File");
            throw new RuntimeException("Report File: ", ioe);
        }

        // Read-Only
        if (cmd.hasOption("ro")) {

        }

        //
        try {
            if (SessionContext.getInstance().getConnectionPools().getQueryAnalysisDataSource() == null) {
                throw new RuntimeException("QueryStore in the config hasn't been successfully setup.");
            }
            execution = new Execution(SessionContext.getInstance().getConnectionPools().getQueryAnalysisDataSource());
        } catch (NullPointerException npe) {
            throw new RuntimeException("QueryStore in the config hasn't been successfully setup..");
        }
        inited = Boolean.TRUE;
    }

    protected String getPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(SessionContext.getInstance().getState().getSelectionQueue().
                stream().map(nav -> nav.getName()).collect(Collectors.joining("->")));
        if (lastScenario != null)
            sb.append("->").append(lastScenario.getName());
        return sb.toString();
    }

    protected void draw() {
//        selections.clear();
        StringBuilder sb = new StringBuilder();
//        sb.append("Path:").append(getPath()).append("\n");
        // Look at the SessionContext to determine where we are.
//        sb.append(ReportingConf.CLEAR_CONSOLE);
        NavState state = SessionContext.getInstance().getState();
        NavigationSelection navigationSelection = state.getSelectionQueue().peekLast();
        int counter = 0;
        if (navigationSelection == null) {
            counter++;
            // At NavTree root.
            sb.append("## Groups ##").append("\n");
            for (String groupKey : SessionContext.getInstance().getNavigationTree().getGroupings().keySet()) {
                Group group = SessionContext.getInstance().getNavigationTree().getGroupings().get(groupKey);
                sb.append("\t").append(counter++).append(". ").append(group.getName()).append("\n");
            }
            sb.append("## Scores ##").append("\n");
            for (String scoreKey : SessionContext.getInstance().getNavigationTree().getScorings().keySet()) {
                Score score = SessionContext.getInstance().getNavigationTree().getScorings().get(scoreKey);
                sb.append("\t").append(counter++).append(". ").append(score.getName()).append("\n");
            }
        } else {
            if (navigationSelection instanceof Group) {
                // List the Options and sub groups.
                sb.append("## Options ##").append("\n");
                for (Option option : ((Group) navigationSelection).getOptions()) {
                    counter++;
                    sb.append("\t").append(counter).append(". ").append(option.getName()).append("\n");
                }
                if (((Group) navigationSelection).getGroupings() != null &&
                        ((Group) navigationSelection).getGroupings().size() > 0) {
                    sb.append("## Groups ##").append("\n");
                    for (String groupKey : ((Group) navigationSelection).getGroupings().keySet()) {
                        counter++;
                        Group group = ((Group) navigationSelection).getGroupings().get(groupKey);
                        sb.append("\t").append(counter).append(". ").append(group.getName()).append("\n");
                    }
                }
            } else if (navigationSelection instanceof Score) {
                Score score = (Score) navigationSelection;
                if (!hasResults()) {
                    sb.append("## Scenario's ##").append("\n");
                    for (Scenario scenario : ((Score) navigationSelection).getScenarios()) {
                        counter++;
                        sb.append("\t").append(counter).append(". ").append(scenario.getName()).append("\n");
                    }
                } else {
                    if (score.hasDetails()) {
                        sb.append("Select record and desired drill-down.").append("\n");
                        sb.append("\tFor example:\n");
                        sb.append("\t\t'2.1' is the 2nd record(#) and 1st sub-selection.").append("\n");
                        sb.append("\t\t'5.2' is the 5th record(#) and 2nd sub-selection.").append("\n");
                        sb.append("## Sub-Selection's ##").append("\n");
                        for (Item item : score.getDetails()) {
                            counter++;
                            sb.append("\t").append(counter).append(". ").append(item.getShortDesc()).append("\n");
                        }
                    }
                }
            } else if (navigationSelection instanceof Option) {
                Option option = (Option) navigationSelection;
                if (!hasResults()) {
                    sb.append("## Scenario's ##").append("\n");
                    for (Scenario scenario : ((Option) navigationSelection).getScenarios()) {
                        counter++;
                        sb.append("\t").append(counter).append(". ").append(scenario.getName()).append("\n");
                    }
                } else {
                    // display options for drilldown OR item.details
                    if (option.hasDrillDowns() | option.getQueryItem().getQuery().hasDetails()) {
                        sb.append("Select record and desired drill-down.").append("\n");
                        sb.append("\tFor example:\n");
                        sb.append("\t\t'2.1' is the 2nd record(#) and 1st sub-selection.").append("\n");
                        sb.append("\t\t'5.2' is the 5th record(#) and 2nd sub-selection.").append("\n");
                        sb.append("## Sub-Selection's ##").append("\n");
                        if (option.hasDrillDowns()) {
                            for (Option drilldown : option.getDrillDowns()) {
                                counter++;
                                sb.append("\t").append(counter).append(". ").append(drilldown.getQueryItem().getShortDesc()).append("\n");
                            }
                        } else {
                            for (Item detailItem : option.getQueryItem().getQuery().getDetails()) {
                                counter++;
                                sb.append("\t").append(counter).append(". ").append(detailItem.getShortDesc()).append("\n");
                            }
                        }
                    }
                }
            }
        }
        System.out.print(sb.toString());
    }

    public void listen() {
        if (!inited) {
            throw new RuntimeException("Need to init CommandLine renderer");
        }
        Scanner scanner = new Scanner(System.in);
        draw();
        try {
            while (true) {
                if (hasResults()) {
                    System.out.print(ReportingConf.ANSI_GREEN + "Make selection or (l-list results, b-back, q-quit)... : "
                            + ReportingConf.ANSI_RESET);
                } else {
                    System.out.print(ReportingConf.ANSI_GREEN + "Make selection or (b-back, q-quit)... : "
                            + ReportingConf.ANSI_RESET);
                }
                String line = scanner.nextLine();
                process(line);
                draw();
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            // exit
        }
    }

    protected void displayPath() {
        System.out.println(ReportingConf.CLEAR_CONSOLE);
        if (SessionContext.getInstance().getState().getSelectionQueue().size() > 0) {
            System.out.println(StringUtils.rightPad(ReportingConf.ANSI_RED, screenWidth, "-"));
            System.out.println(StringUtils.center(getPath(), screenWidth, " "));
            System.out.println(StringUtils.leftPad(ReportingConf.ANSI_RESET, screenWidth, "-"));
        }
    }

    protected void process(String input) {
        NavState navState = SessionContext.getInstance().getState();
        Integer[] value = null;
        // Parse the selection.
        try {
            String[] inputs = input.split("\\.");
            if (inputs.length > 0) {
                value = new Integer[inputs.length];
                int lpos = 0;
                for (String in : inputs) {
                    value[lpos++] = Integer.valueOf(in);
                }
            } else {
                value = new Integer[1];
                value[0] = Integer.valueOf(input);
            }
        } catch (NumberFormatException nfe) {
            if (input == null || StringUtils.isEmpty(input)) {
                clearResults();
                navState.getSelectionQueue().pollLast();
                System.out.println(ReportingConf.CLEAR_CONSOLE);
                displayPath();
            } else {
                switch (input.toLowerCase()) {
                    case "b":
                        clearResults();
                        navState.getSelectionQueue().pollLast();
                        System.out.println(ReportingConf.CLEAR_CONSOLE);
                        displayPath();
                        break;
                    case "q":
                        System.exit(0);
                        break;
                    case "l":
                        if (hasResults()) {
                            System.out.println(ReportingConf.CLEAR_CONSOLE);
                            displayPath();
                            screen.draw(currentResults, currentDisplay, System.out);
                        }
                    default:
                        break;
                }
            }
            return;
        }
        try {
            int counter = 0;
            // If we're at the top of the tree.
            if (navState.getSelectionQueue().peek() == null) {
                NavigationTree navigationTree = SessionContext.getInstance().getNavigationTree();
                for (String groupKey : navigationTree.getGroupings().keySet()) {
                    counter++;
                    if (counter == value[0]) {
                        navState.addPath(navigationTree.getGroupings().get(groupKey));
                        displayPath();
                        break;
                    }
                }
                for (String scoreKey : navigationTree.getScorings().keySet()) {
                    counter++;
                    if (counter == value[0]) {
                        navState.addPath(navigationTree.getScorings().get(scoreKey));
                        displayPath();
                        break;
                    }
                }
            } else {
                // Get Last item in the path selection.
                NavigationSelection current = navState.getSelectionQueue().peekLast();
                if (current instanceof Group) {
                    Group currentGroup = (Group) current;
                    for (Option option : currentGroup.getOptions()) {
                        counter++;
                        if (counter == value[0]) {
                            navState.addPath(option);
                            System.out.println(ReportingConf.CLEAR_CONSOLE);
                            displayPath();
                            break;
                        }
                    }
                    for (String groupKey : currentGroup.getGroupings().keySet()) {
                        counter++;
                        if (counter == value[0]) {
                            navState.addPath(currentGroup.getGroupings().get(groupKey));
                            System.out.println(ReportingConf.CLEAR_CONSOLE);
                            displayPath();
                            break;
                        }
                    }
                }
                if (current instanceof Option) {
                    Option option = (Option) current;
                    // If currentResults == null
                    if (!hasResults()) {
                        for (Scenario scenario : option.getScenarios()) {
                            counter++;
                            TokenReplacement tr = TokenReplacement.getInstance();
                            if (counter == value[0]) {
                                lastScenario = scenario; // Set the last scenario
                                Map<String, Object> wrkParams = new HashMap<String, Object>(scenario.getParameters());
                                // Review the 'scenario' parameters for 'tokens'.
                                List<String> parameterTokens = tr.listTokens(scenario.getParameters());
                                if (parameterTokens.size() > 0) {
                                    Map<String, Object> resolvedParams = new HashMap<String, Object>();
                                    Scanner scanner = new Scanner(System.in);
                                    for (String token : parameterTokens) {
                                        // Ask for the feedback.
                                        System.out.print(token + ":");
                                        String line = scanner.nextLine();
                                        resolvedParams.put(token, line);
                                    }
                                    // Need to resolve tokens before submitting.
                                    wrkParams = tr.replaceInMap(wrkParams, resolvedParams);
                                }
                                // Run Option w/ selected Scenario.
                                // Do we need to create another container for settings, results, and selection?
                                try {
                                    currentResults = execution.run(option, wrkParams);
                                    currentDisplay = new ArrayList<String>(option.getDisplay());
                                    if (currentResults != null && currentResults.getRecords().size() > 0) {
                                        System.out.println(ReportingConf.CLEAR_CONSOLE);
                                        displayPath();
                                        output.println("# " + getPath());
                                        screen.draw(currentResults, currentDisplay, System.out);
                                        formatter.draw(currentResults, output);
                                        if (!(option.hasDrillDowns() || option.getQueryItem().getQuery().hasDetails())) {
                                            clearResults();
                                        }
                                    } else {
                                        System.out.println(ReportingConf.CLEAR_CONSOLE);
                                        displayPath();
                                        System.out.println(" -- empty results --");
                                        clearResults();
                                    }
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    } else {
                        // if there are drilldown, the second selection is for that selection.
                        // else check for 'detail' selections of the query.
                        if (option.hasDrillDowns() || option.getQueryItem().getQuery().hasDetails()) {
                            Map<String, Object> selectedRecord = currentResults.getRecord(value[0] - 1);
                            if (option.hasDrillDowns()) {
                                Option dd = option.getDrillDowns().get(value[1] - 1);

                                navState.getSelectionQueue().add(dd);
                                try {
                                    currentResults = execution.run(dd.getQueryItem(), selectedRecord, lastScenario);
                                    currentDisplay = null;
                                    System.out.println(ReportingConf.CLEAR_CONSOLE);
                                    displayPath();
                                    output.println("# " + getPath() + "-> (detail)");
                                    // TODO: Print to screen the 'selectedRecord' before the detail.
                                    screen.draw(currentResults, dd.getQueryItem().getDisplay(), System.out);
                                    formatter.draw(currentResults, output);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (option.getQueryItem().getQuery().hasDetails()) {

                                Item qI = option.getQueryItem().getQuery().getDetails().get(value[1] - 1);

                                try {
                                    // Get parameters of previous selection. IE: To align the Interval for selections
                                    //    that might be more than a detailed view.
                                    System.out.println(ReportingConf.CLEAR_CONSOLE);
                                    ResultArray ddRA = execution.run(qI, selectedRecord, lastScenario);
                                    String curPath = "## " + getPath();
                                    displayPath();
                                    output.println(curPath);
                                    // TODO: Print the 'selectedRecord' to screen before the detail.
                                    screen.draw(currentResults, selectedRecord, System.out);
                                    screen.draw(ddRA, qI.getDisplay(), System.out);
                                    formatter.draw(ddRA, output);
                                    // TODO: Do we want to 're-display' the list here?  From a Screen perspective, it would
                                    //              hide the detail output.
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
                if (current instanceof Score) {
                    Score score = (Score) current;
                    // If currentResults == null
                    if (!hasResults()) {
                        for (Scenario scenario : score.getScenarios()) {
                            counter++;
                            TokenReplacement tr = TokenReplacement.getInstance();
                            if (counter == value[0]) {
                                lastScenario = scenario; // Set the last scenario
                                Map<String, Object> wrkParams = new HashMap<String, Object>(scenario.getParameters());
                                // Review the 'scenario' parameters for 'tokens'.
                                List<String> parameterTokens = tr.listTokens(scenario.getParameters());
                                if (parameterTokens.size() > 0) {
                                    Map<String, Object> resolvedParams = new HashMap<String, Object>();
                                    Scanner scanner = new Scanner(System.in);
                                    for (String token : parameterTokens) {
                                        // Ask for the feedback.
                                        System.out.print(token + ":");
                                        String line = scanner.nextLine();
                                        resolvedParams.put(token, line);
                                    }
                                    // Need to resolve tokens before submitting.
                                    wrkParams = tr.replaceInMap(wrkParams, resolvedParams);
                                }
                                // Run Option w/ selected Scenario.
                                // Do we need to create another container for settings, results, and selection?
                                try {
                                    Integer resultLimit = 10;
                                    if (wrkParams.get("limit") != null) {
                                        resultLimit = Integer.parseInt(wrkParams.get("limit").toString());
                                    }
                                    currentResults = execution.runScoreRanking(score, score.getScoringKeyField(),
                                            score.getDisplay(), wrkParams, resultLimit);
                                    List<String> wrkDisplay = new ArrayList<String>();
                                    //score.getDisplay();
                                    wrkDisplay.add("rank");
                                    wrkDisplay.add(score.getScoringKeyField());
                                    wrkDisplay.addAll(score.getDisplay());
                                    currentDisplay = new ArrayList<String>(wrkDisplay);
                                    if (currentResults != null && currentResults.getRecords().size() > 0) {
                                        System.out.println(ReportingConf.CLEAR_CONSOLE);
                                        displayPath();
                                        output.println("# " + getPath());
                                        screen.draw(currentResults, currentDisplay, System.out);
                                        formatter.draw(currentResults, output);
                                    } else {
                                        System.out.println(ReportingConf.CLEAR_CONSOLE);
                                        displayPath();
                                        System.out.println(" -- empty results --");
                                        clearResults();
                                    }
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    } else {
                        // if there are drilldown, the second selection is for that selection.
                        // else check for 'detail' selections of the query.
                        if (score.hasDetails()) {
                            Map<String, Object> selectedRecord = currentResults.getRecord(value[0] - 1);
                                Item qI = score.getDetails().get(value[1] - 1);
                                try {
                                    // Get parameters of previous selection. IE: To align the Interval for selections
                                    //    that might be more than a detailed view.
                                    System.out.println(ReportingConf.CLEAR_CONSOLE);
                                    ResultArray ddRA = execution.run(qI, selectedRecord, lastScenario);
                                    String curPath = "## " + getPath();
                                    displayPath();
                                    output.println(curPath);
                                    // TODO: Print the 'selectedRecord' to screen before the detail.
                                    screen.draw(currentResults, selectedRecord, System.out);
                                    screen.draw(ddRA, qI.getDisplay(), System.out);
                                    formatter.draw(ddRA, output);
                                    // TODO: Do we want to 're-display' the list here?  From a Screen perspective, it would
                                    //              hide the detail output.
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }

                        }
                    }
                }

            }
        } catch (ArrayIndexOutOfBoundsException aiob) {
            System.out.println("Invalid Selection: " + input + "\tClearing results.");
            clearResults();
        } catch (IndexOutOfBoundsException ioobe) {
            System.out.println(ReportingConf.CLEAR_CONSOLE);
            System.out.println("Invalid Selection");
            if (hasResults()) {
                displayPath();
                screen.draw(currentResults, currentDisplay, System.out);
            }

        }
    }

    public void run() {
        listen();
        if (output != null) {
            output.close();
        }
    }
}
