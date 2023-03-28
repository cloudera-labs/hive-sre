package com.cloudera.framework.sqlreports.format;

import com.cloudera.utils.sql.ResultArray;

import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public interface Formatter {

    void draw (ResultArray resultArray, PrintStream outputStream);
    void draw(ResultArray resultArray, List<String> display, PrintStream outputStream);
    void draw (ResultArray resultArray, Map<String, Object> selectedRecord, PrintStream outputStream);
    void draw(ResultArray resultArray, List<String> display, Map<String,Object> selectedRecord, PrintStream outputStream);

//    void draw (ResultArray resultArray, Writer writer);
}
