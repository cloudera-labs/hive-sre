package com.cloudera.framework.sqlreports.format;

import com.cloudera.utils.hive.reporting.ReportingConf;
import com.cloudera.utils.sql.ResultArray;
import com.cloudera.utils.sql.TypeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Screen implements Formatter {

    private static Logger LOG = LogManager.getLogger(Screen.class);
    private int screenWidth = 100;

    public Screen() {
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

    private final String DELIMITER = " | ";

    enum Position {
        LEFT, CENTER, RIGHT;
    }

    private class Column {
        Position alignment;
        int width;

        public Column(Position alignment, int width) {
            this.alignment = alignment;
            this.width = width;
        }
    }

    private final String recName = "#";

    private Column[] getAlignment(ResultArray resultArray, List<String> fields) {
        List<Column> columns = new ArrayList<Column>();

        // Used to setup a ROWID for display.
        if (resultArray.getRecords().size() > 1) {
            int rW = Integer.toString(resultArray.getRecords().size()).length() + 1;
            Column rCol = new Column(Position.RIGHT, rW);
            columns.add(rCol);
        }
        for (String field : fields) {
            String[] columnFields = resultArray.getColumn(field);
            int fMax = field.length();
            // Get the max width for the fields in the column.
            for (String columnField : columnFields) {
                if (columnField != null)
                    if (fMax < columnField.length())
                        fMax = columnField.length();
            }
            switch (TypeUtils.getAlignment(resultArray.getFieldType(field))) {
                case LEFT_ALIGNMENT:
                    columns.add(new Column(Position.LEFT, fMax));
                    break;
                case CENTER_ALIGNMENT:
                    columns.add(new Column(Position.CENTER, fMax));
                    break;
                case RIGHT_ALIGNMENT:
                    columns.add(new Column(Position.RIGHT, fMax));
                    break;
            }
        }
        Column[] cRtn = new Column[columns.size()];
        return columns.toArray(cRtn);
    }

    private int getTotalAlignmentWidth(Column[] columns) {
        int width = 0;
        for (Column column : columns) {
            width = width + column.width;
        }
//      Add delimiter width, which will be delimiter * (field count - 1)
        width = width + (DELIMITER.length() * (columns.length - 1));
        return width;
    }

    private String alignmentPad(String fieldValue, Column alignment) {
        String rtn = null;
        switch (alignment.alignment) {
            case LEFT:
                rtn = StringUtils.rightPad(fieldValue, alignment.width, " ");
                break;
            case CENTER:
                rtn = StringUtils.center(fieldValue, alignment.width, " ");
                break;
            case RIGHT:
                rtn = StringUtils.leftPad(fieldValue, alignment.width, " ");
                break;
        }
        return rtn;
    }

    @Override
    public void draw(ResultArray resultArray, Map<String, Object> selectedRecord, PrintStream outputStream) {
        draw(resultArray, null, selectedRecord, outputStream);
    }

    @Override
    public void draw(ResultArray resultArray, List<String> display, Map<String, Object> selectedRecord, PrintStream outputStream) {
        outputStream.println(build(resultArray, display, selectedRecord));
    }

    protected String build(ResultArray resultArray, List<String> fields, Map<String, Object> selectedRecord) {
        StringBuilder sb = new StringBuilder();
        resultArray.fixDisplayFields(fields);

        int loc = 1;
        List<String> wrkFields = null;
        if (fields == null || fields.size() == 0) {
            wrkFields = new ArrayList<String>(resultArray.getHeader());
        } else {
            wrkFields = fields;
        }

        Column[] alignment = getAlignment(resultArray, wrkFields);
        Boolean ROWID = Boolean.FALSE;
        sb.append(StringUtils.center(ReportingConf.ANSI_BLUE + "<<<<<<<<>>>>>>>  begin source <<<<<<<<>>>>>>>" + ReportingConf.ANSI_RED, getTotalAlignmentWidth(alignment)));
        sb.append("\n");
        int rPos = 0;
        if (resultArray.getRecords().size() > 1) {
            ROWID = Boolean.TRUE;
            sb.append(alignmentPad(recName, alignment[rPos++])).append(DELIMITER);
        }
        for (String fieldName : wrkFields) {
            sb.append(alignmentPad(fieldName, alignment[rPos++])).append(DELIMITER);
        }
        sb.append(ReportingConf.ANSI_BLUE + "\n");
        sb.append(StringUtils.leftPad("-", getTotalAlignmentWidth(alignment), "-"));
        sb.append("\n" + ReportingConf.ANSI_GREEN);

        // Don't number results if there is only 1.
        rPos = 0;
        if (ROWID) {
            sb.append(alignmentPad(Integer.toString(loc++), alignment[rPos++])).append(DELIMITER);
        }
        for (String fieldKey : selectedRecord.keySet()) {
            Object fieldValue = selectedRecord.get(fieldKey);
            if (fieldValue != null)
                sb.append(alignmentPad(fieldValue.toString(), alignment[rPos++])).append(DELIMITER);
            else
                sb.append(alignmentPad(" ", alignment[rPos++])).append(DELIMITER);
        }
        sb.append("\n");
        sb.append(StringUtils.center(ReportingConf.ANSI_BLUE + "<<<<<<<<>>>>>>>>  source end <<<<<<<<>>>>>>>\n" + ReportingConf.ANSI_RESET, getTotalAlignmentWidth(alignment)));
        return sb.toString();
    }

    protected String build(ResultArray resultArray, List<String> fields) {
        StringBuilder sb = new StringBuilder();
        resultArray.fixDisplayFields(fields);

        int loc = 1;
        List<String> wrkFields = null;
        if (fields == null || fields.size() == 0) {
            wrkFields = new ArrayList<String>(resultArray.getHeader());
        } else {
            wrkFields = fields;
        }

        Column[] alignment = getAlignment(resultArray, wrkFields);
        Boolean ROWID = Boolean.FALSE;
        sb.append(StringUtils.center(ReportingConf.ANSI_BLUE + "<<<<<<<<>>>>>>>  begin results <<<<<<<<>>>>>>>" + ReportingConf.ANSI_RED, getTotalAlignmentWidth(alignment)));
        sb.append("\n");
        int rPos = 0;
        if (resultArray.getRecords().size() > 1) {
            ROWID = Boolean.TRUE;
            sb.append(alignmentPad(recName, alignment[rPos++])).append(DELIMITER);
        }
        for (String fieldName : wrkFields) {
            sb.append(alignmentPad(fieldName, alignment[rPos++])).append(DELIMITER);
        }
        sb.append(ReportingConf.ANSI_BLUE + "\n");
        sb.append(StringUtils.leftPad("-", getTotalAlignmentWidth(alignment), "-"));
        sb.append("\n" + ReportingConf.ANSI_GREEN);

        for (Object[] record : resultArray.getRecords(wrkFields)) {
            // Don't number results if there is only 1.
            rPos = 0;
            if (ROWID) {
                sb.append(alignmentPad(Integer.toString(loc++), alignment[rPos++])).append(DELIMITER);
            }
            for (Object fieldValue : record) {
                if (fieldValue != null)
                    sb.append(alignmentPad(fieldValue.toString(), alignment[rPos++])).append(DELIMITER);
                else
                    sb.append(alignmentPad(" ", alignment[rPos++])).append(DELIMITER);
            }
            sb.append("\n");
        }
        sb.append(StringUtils.center(ReportingConf.ANSI_BLUE + "<<<<<<<<>>>>>>>>  results end <<<<<<<<>>>>>>>\n" + ReportingConf.ANSI_RESET, getTotalAlignmentWidth(alignment)));
        return sb.toString();
    }

    @Override
    public void draw(ResultArray resultArray, PrintStream outputStream) {
        outputStream.println(build(resultArray, null));
    }

    @Override
    public void draw(ResultArray resultArray, List<String> fields, PrintStream outputStream) {
        outputStream.println(build(resultArray, fields));
    }
}
