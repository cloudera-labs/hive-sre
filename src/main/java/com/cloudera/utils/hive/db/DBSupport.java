package com.cloudera.utils.hive.db;

public class DBSupport {
    public enum TYPE {
        MYSQL("^10\\.[1-2]+\\.\\d+\\-Maria.*|^5\\.7\\.\\d+"),
        POSTGRESQL("^PostgreSQL\\s([9]\\.[3-4,6].*|10\\.[2,5,7]|11)"),
        MSSQL(""),
        ORACLE("");

        private String supportedRegEx;

        TYPE(String supportedRegEx) {
            this.supportedRegEx = supportedRegEx;
        }
    }


}
