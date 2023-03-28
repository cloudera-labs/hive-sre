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

package com.cloudera.utils.sql;

import com.cloudera.utils.common.CompressionUtil;
import com.cloudera.utils.hive.sre.DbSetProcess;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.sql.Types.*;
import static java.sql.Types.TIMESTAMP;

public class ResultArray {
    private static Logger LOG = LogManager.getLogger(ResultArray.class);
//    private String[] header;
    private Map<String, Integer> fields = new LinkedHashMap<String, Integer>();
//    private Map<String, Integer> fields = new HashMap<String, Integer>();

    private int columnCount = 0;
    private List<Object[]> records = new ArrayList<Object[]>();

    public ResultArray() {
    }

    public ResultArray(ResultSet source) {
        build(source);
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public Set<String> getHeader() {
        return fields.keySet();
    }

    public List<Object[]> getRecords() {
        return records;
    }

    public void fixDisplayFields(List<String> displayFields) {
        if (displayFields != null) {
            List<String> wrkDF = new ArrayList<String>(displayFields);
            for (String field : getHeader()) {
                for (String displayField : wrkDF) {
                    if (!getHeader().contains(displayField)) {
                        LOG.warn("Removed display field: " + displayField + ". It doesn't exist in the header: " + getHeader());
                        displayFields.remove(displayField);
                    }
                }
            }
        }
    }

    public List<Object[]> getRecords(List<String> fields) {
        List<Integer> intF = new ArrayList<Integer>();
        int pos = 0;
        int loc = 0;
        // Get field locations from Header
        for (String field: getHeader()) {
            for (String displayField: fields) {
                if (field.equalsIgnoreCase(displayField)) {
                    intF.add(pos);
                    break;
                }
            }
            // Increment header field location
            pos++;
        }
        if (fields.size() != intF.size()) {
            LOG.warn("Filtered Display Fields: " + fields);
            LOG.warn("Available Header Fields: " + getHeader());
            LOG.warn("Display/Header field mis-match");
        }
        // Build return of just those fields.
        List<Object[]> rtn = new ArrayList<Object[]>();
        for (Object[] record: getRecords()) {
            // As long as there are display fields, filter
            if (fields != null && fields.size() > 0) {
                List lRecord = new ArrayList();
                for (Integer ipos : intF) {
                    lRecord.add(record[ipos]);
                }
                rtn.add(lRecord.toArray());
            } else {
                rtn.add(record);
            }
        }
        return rtn;
    }

    public Map<String, Object> getRecord(int row) {
        Map<String, Object> rtn = null;
        Object[] record = getRecords().get(row);
        if (record != null) {
            rtn = new LinkedHashMap<String, Object>();
            int pos = 0;
            for (String field: getHeader()) {
                rtn.put(field, record[pos++]);
            }
        }
        return rtn;
    }

    public void remove(String removeFilter, int index) {
        Pattern rPat = Pattern.compile(removeFilter);
        List<Object[]> newRecordsList = new ArrayList<Object[]>();
        for (Object[] item: records) {
            if (!rPat.matcher(item[index].toString()).find()) {
                newRecordsList.add(item);
            }
        }
        records = newRecordsList;
    }

    public void keep(String keepFilter, int index) {
        Pattern rPat = Pattern.compile(keepFilter);
        List<Object[]> newRecordsList = new ArrayList<Object[]>();
        for (Object[] item: records) {
            if (rPat.matcher(item[index].toString()).find()) {
                newRecordsList.add(item);
            }
        }
        records = newRecordsList;
    }

    private void build(ResultSet resultSet) {
        try {
            ResultSetMetaData metadata = resultSet.getMetaData();
            columnCount = metadata.getColumnCount();
            fields.clear(); // ensure this is empty before we start.
            for (int i = 1;i <= columnCount; i++) {
                String column = metadata.getColumnName(i);
                Integer type = metadata.getColumnType(i);
                fields.put(column, type);
            }

            List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(fields.entrySet());
            Map.Entry<String, Integer> lastEntry = entryList.get(entryList.size()-1);

            String lastField = lastEntry.getKey();

            while (resultSet.next()) {
                Object[] record = new Object[columnCount];
                int i = 0;
                for (String column: fields.keySet()) {
                    // Check the Column Type and Fetch.
                    switch (fields.get(column)) {
                        case BIT:
                            record[i++] = resultSet.getBoolean(column);
                            break;
                        case TINYINT:
                        case SMALLINT:
                        case INTEGER:
                            record[i++] =  resultSet.getInt(column);
                            break;
                        case BIGINT:
                            record[i++] = resultSet.getLong(column);
                            break;
                        case FLOAT:
                            record[i++] = resultSet.getFloat(column);
                            break;
                        case REAL:
                        case DOUBLE:
                        case NUMERIC:
                            record[i++] = resultSet.getDouble(column);
                            break;
                        case DECIMAL:
                            record[i++] = resultSet.getBigDecimal(column);
                            break;
                        case CHAR:
                        case VARCHAR:
                        case LONGVARCHAR:
                            record[i++] = resultSet.getString(column);
                            break;
                            // TODO: HANDLE Date/Time Formatting
                        case DATE:
                            record[i++] = resultSet.getDate(column);
                            break;
                        case TIME:
                            record[i++] = resultSet.getTime(column);
                            break;
                        case TIMESTAMP:
                            record[i++] = resultSet.getTimestamp(column);
                            break;
                        case OTHER:
                            record[i++] = resultSet.getString(column);
                            break;
                        case BINARY:
                            // TODO: Figure a way to run this declaritively. For now, assuming ALL binary fields get decompressed
                            InputStream is = resultSet.getBinaryStream(column);
                            byte[] targetArray = new byte[is.available()];
                            is.read(targetArray);
                            CompressionUtil cu = CompressionUtil.getInstance();
                            String bStr = new String(cu.decompress(targetArray));
                            record[i++] = bStr;
                            break;
                    }
                }
                records.add(record);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String[] getColumn(String name) {
        int columnIndex = find(fields.keySet().toArray(new String[0]), name);
        String[] rtn = new String[records.size()];
        int i = 0;
        for (Object[] record: records) {
            if (record[columnIndex] != null) {
                rtn[i++] = record[columnIndex].toString();
            } else {
                i++;
            }
        }
        return rtn;
    }

    public String[][] getColumns(String[] columns) {
        int[] columnIndexes = new int[columns.length];
        String[][] rtn = null;
        if (columns.length > 0) {
            rtn = new String[columns.length][records.size()];

            for (int i = 0; i < columns.length; i++) {
                int columnIndex = find(fields.keySet().toArray(new String[0]), columns[i]);
                if (columnIndex == -1) {
                    LOG.error("Mis-match of columns in Resultset and the (db)ListingColumns: " + Arrays.toString(columns) +
                            " vs. " + Arrays.toString(fields.keySet().toArray(new String[0])));
                    throw new RuntimeException("Mis-match of columns in Resultset and the (db)ListingColumns: " + Arrays.toString(columns) +
                            " vs. " + Arrays.toString(fields.keySet().toArray(new String[0])));
                } else {
                    columnIndexes[i] = find(fields.keySet().toArray(new String[0]), columns[i]);
                }
            }

            int i = 0;
            for (Object[] record : records) {
                for (int c = 0; c < columnIndexes.length; c++) {
                    try {
                        if (record[columnIndexes[c]] != null) {
                            rtn[c][i] = record[columnIndexes[c]].toString();
                        }
                    } catch (ArrayIndexOutOfBoundsException aiobe) {
                        LOG.error(Arrays.toString(record) + ":" + Arrays.toString(columnIndexes), aiobe);
                        throw new RuntimeException(Arrays.toString(record) + ":" + Arrays.toString(columnIndexes), aiobe);
                    } catch (NullPointerException npe) {
                        LOG.error(npe);
                        throw npe;
                    }
                }
                i++;
            }
        }
        return rtn;
    }

    public long getCount() {
        return records.size();
    }

    public String getField(String column, Integer index) {
        // Find the column index.
        int columnIndex = find(fields.keySet().toArray(new String[0]), column);
        // Pull the record
        Object[] record = records.get(index);
        // return the field
        return record[columnIndex].toString();
    }

    public Map<String, Integer> getFields() {
        return fields;
    }

    public int getFieldType(String fieldName) {
        return getFields().get(fieldName);
    }
    // Function to find the index of an element in a primitive array in Java
    public static int find(String[] a, String search) {
        return IntStream.range(0, a.length)
                .filter(i -> search.equalsIgnoreCase(a[i]))
                .findFirst()
                .orElse(-1);	// return -1 if target is not found
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HEADER\n" + Arrays.toString(fields.keySet().toArray(new String[0])) + "\n");
        sb.append("RECORDS\n");
        for (Object[] record: records) {
            sb.append(Arrays.toString(record)).append("\n");
        }
        return sb.toString();
    }
}
