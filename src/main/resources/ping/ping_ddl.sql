-- Will DROP ALL DATA from previous run
DROP DATABASE IF EXISTS ${PING_DB} CASCADE;
CREATE DATABASE IF NOT EXISTS ${PING_DB};
USE ${PING_DB};

DROP TABLE IF EXISTS RAW;
CREATE EXTERNAL TABLE RAW
(
    TIME_STAMP  TIMESTAMP,
    SOURCE_IP   STRING,
    SOURCE_HOST STRING,
    TARGET_HOST STRING,
    STATUS      INT,
    LETANCY     DOUBLE
)
    PARTITIONED BY (BATCH_ID STRING)
    TBLPROPERTIES ('discover.partitions' = 'true');

DROP TABLE IF EXISTS PING;
CREATE TABLE PING
(
    BATCH_ID    STRING,
    TIME_STAMP  TIMESTAMP,
    SOURCE_IP   STRING,
    SOURCE_HOST STRING,
    TARGET_HOST STRING,
    STATUS      INT,
    LETANCY     DOUBLE
);
