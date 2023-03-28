package com.cloudera.utils.sql;

import static java.sql.Types.*;

public class TypeUtils {
    public enum ALIGNMENT {
        LEFT_ALIGNMENT,
        CENTER_ALIGNMENT,
        RIGHT_ALIGNMENT;
    }

    public static ALIGNMENT getAlignment(int sqlType) {
        ALIGNMENT rtn = ALIGNMENT.LEFT_ALIGNMENT;
        switch (sqlType) {
            case REF_CURSOR:
            case SQLXML:
            case NCLOB:
            case LONGNVARCHAR:
            case NVARCHAR:
            case NCHAR:
            case REF:
            case CLOB:
            case BLOB:
            case ARRAY:
            case STRUCT:
            case DISTINCT:
            case JAVA_OBJECT:
            case OTHER:
            case LONGVARBINARY:
            case VARBINARY:
            case BINARY:
            case LONGVARCHAR:
            case VARCHAR:
            case CHAR:
                rtn = ALIGNMENT.LEFT_ALIGNMENT;
                break;
            case TIMESTAMP_WITH_TIMEZONE:
            case TIME_WITH_TIMEZONE:
            case ROWID:
            case BOOLEAN:
            case DATALINK:
            case NULL:
            case TIMESTAMP:
            case TIME:
            case DATE:
            case DECIMAL:
            case NUMERIC:
            case DOUBLE:
            case REAL:
            case FLOAT:
            case BIGINT:
            case INTEGER:
            case BIT:
            case SMALLINT:
            case TINYINT:
                rtn = ALIGNMENT.RIGHT_ALIGNMENT;
                break;
        }
        return rtn;
    }


}
