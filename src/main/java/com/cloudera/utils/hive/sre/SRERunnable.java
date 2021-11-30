package com.cloudera.utils.hive.sre;

import java.io.PrintStream;
import java.util.concurrent.Callable;

public abstract class SRERunnable implements Callable<String> {

    private String displayName;
//    private TaskState state = TaskState.CONSTRUCTED;

    /**
     * allows stdout to be captured if necessary
     */
    public PrintStream success = System.out;
    /**
     * allows stderr to be captured if necessary
     */
    public PrintStream error = System.err;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public abstract Boolean init();

}
