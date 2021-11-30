package com.cloudera.utils.sql;

import java.sql.*;
import java.util.*;

import static java.sql.Types.*;
import static java.sql.Types.TIMESTAMP;

public class JDBCUtils {

    public static PreparedStatement getPreparedStatement(Connection conn, QueryDefinition query) {
        PreparedStatement rtn = null;
        try {
            rtn = conn.prepareStatement(query.getStatement());
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return rtn;
    }

    public static void setPreparedStatementParameters(PreparedStatement preparedStatement, QueryDefinition query,
                                                      Properties overrides) {
        Properties lclOverrides = overrides;
        if (lclOverrides == null) {
            lclOverrides = new Properties();
        }
        try {
            if (query.getParameters() != null) {
                for (String key : query.getParameters().keySet()) {
                    Parameter param = query.getParameters().get(key);
                    String value = lclOverrides.getProperty(key, param.getInitial());
                    switch (param.getSqlType()) {
                        case VARCHAR:
                            preparedStatement.setString(param.getLocation(), value);
                            break;
                    }
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }


    public static void setPreparedStatementParameters(PreparedStatement preparedStatement, QueryDefinition query,
                                                      QueryDefinition override) {
        try {
            if (query.getParameters() != null) {
                for (String key : query.getParameters().keySet()) {
                    Parameter param = query.getParameters().get(key);
                    String value = param.getInitial();
                    if (override != null && override.getParameters().get(key) != null) {
                        value = override.getParameters().get(key).getOverride();
                    }
                    switch (param.getSqlType()) {
                        case VARCHAR:
                            preparedStatement.setString(param.getLocation(), value);
                            break;
                    }
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public static void displayResultSet(ResultSet resultSet, Boolean header) {
        try {
            String delimiter = "\t";
            String newLine = "\n";
            StringBuilder sb = new StringBuilder();
            ResultSetMetaData metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount();
            Map<String, Integer> fields = new LinkedHashMap<String, Integer>();
            for (int i = 1; i <= columnCount; i++) {
                String column = metadata.getColumnName(i);
                Integer type = metadata.getColumnType(i);
                fields.put(column, type);
                if (header) {
                    sb.append(metadata.getColumnName(i));
                    if (i < metadata.getColumnCount())
                        sb.append(delimiter);
                }
            }
            if (header)
                sb.append(newLine);
            List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(fields.entrySet());
            Map.Entry<String, Integer> lastEntry = entryList.get(entryList.size() - 1);

            String lastField = lastEntry.getKey();

            while (resultSet.next()) {

                for (String key : fields.keySet()) {
                    switch (fields.get(key)) {
                        case BIT:
                            sb.append(resultSet.getByte(key));
                            break;
                        case TINYINT:
                        case SMALLINT:
                        case INTEGER:
                            sb.append(resultSet.getInt(key));
                            break;
                        case BIGINT:
                            sb.append(resultSet.getLong(key));
                            break;
                        case FLOAT:
                            sb.append(resultSet.getFloat(key));
                            break;
                        case REAL:
                        case DOUBLE:
                        case NUMERIC:
                            sb.append(resultSet.getDouble(key));
                            break;
                        case DECIMAL:
                            sb.append(resultSet.getBigDecimal(key));
                            break;
                        case CHAR:
                        case VARCHAR:
                        case LONGVARCHAR:
                            sb.append(resultSet.getString(key));
                            break;
                        case DATE:
                            sb.append(resultSet.getDate(key));
                            break;
                        case TIME:
                            sb.append(resultSet.getTime(key));
                            break;
                        case TIMESTAMP:
                            sb.append(resultSet.getTimestamp(key));
                            break;
                    }
                    if (lastField.equals(key)) {
                        sb.append(newLine);
                    } else {
                        sb.append(delimiter);
                    }
                }

            }
            System.out.print(sb.toString());
        } catch (SQLException se) {

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
