package com.cloudera.framework.sqlreports.format;

import com.cloudera.utils.sql.ResultArray;
import com.cloudera.utils.sql.TypeUtils;

import static com.cloudera.utils.sql.TypeUtils.ALIGNMENT.*;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Markdown implements Formatter {

    private final String TABLE_DELIMITER = "|";
    private final String MD_LEFT_ALIGN = ":---";
    private final String MD_CENTER_ALIGN = "---";
    private final String MD_RIGHT_ALIGN = "---:";

    protected String build(ResultArray resultArray) {
        StringBuilder sb = new StringBuilder();
        // Header
        sb.append(TABLE_DELIMITER);
        for (String fieldName : resultArray.getHeader()) {
            sb.append(fieldName).append(TABLE_DELIMITER);
        }
        sb.append("\n");
        // Format
        sb.append(TABLE_DELIMITER);
        for (String fieldName : resultArray.getHeader()) {
            switch (TypeUtils.getAlignment(resultArray.getFieldType(fieldName))) {
                case LEFT_ALIGNMENT:
                    sb.append(MD_LEFT_ALIGN);
                    break;
                case CENTER_ALIGNMENT:
                    sb.append(MD_CENTER_ALIGN);
                    break;
                case RIGHT_ALIGNMENT:
                    sb.append(MD_RIGHT_ALIGN);
                    break;
            }
            sb.append(TABLE_DELIMITER);
        }
        sb.append("\n");
        // Records
        for (Object[] record : resultArray.getRecords()) {
            sb.append(TABLE_DELIMITER);
            sb.append(StringEscapeUtils.escapeJava(
                    Arrays.stream(record).map(i -> i!=null?i.toString():"").collect(Collectors.joining(TABLE_DELIMITER))));
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void draw(ResultArray resultArray, PrintStream outputStream) {
        outputStream.println(build(resultArray));
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

    //    @Override
//    public void draw(ResultArray resultArray, Writer writer) {
//        try {
//            writer.write(build(resultArray));
//            writer.write("\n");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
