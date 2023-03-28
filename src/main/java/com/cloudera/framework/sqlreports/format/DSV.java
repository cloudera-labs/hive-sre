package com.cloudera.framework.sqlreports.format;

import com.cloudera.utils.sql.ResultArray;

import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class DSV implements Formatter {

    private final String DELIMITER = "|";
    private final String QUOTE = "\"";

    @Override
    public void draw(ResultArray resultArray, PrintStream outputStream) {

    }

    @Override
    public void draw(ResultArray resultArray, List<String> display, PrintStream outputStream) {

    }

    @Override
    public void draw(ResultArray resultArray, Map<String, Object> selectedRecord, PrintStream outputStream) {

    }

    @Override
    public void draw(ResultArray resultArray, List<String> display, Map<String, Object> selectedRecord, PrintStream outputStream) {

    }
}
