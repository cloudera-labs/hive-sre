CREATE DATABASE IF NOT EXISTS ${DB};

USE ${DB};

DROP TABLE IF EXISTS TBL_VOL_RPT;
CREATE EXTERNAL TABLE TBL_VOL_RPT
(
    empty      STRING,
    db         STRING,
    tbl        STRING,
    tbl_type   STRING,
    part       STRING,
    loc        STRING,
    dir_count  BIGINT,
    file_count BIGINT,
    loc_size   STRING COMMENT "need to convert this from human readable"
)
    ROW FORMAT SERDE
        'org.apache.hadoop.hive.serde2.OpenCSVSerde'
        WITH SERDEPROPERTIES (
        "separatorChar" = "|"
        )
    STORED AS TEXTFILE
    LOCATION "${LOCATION}";

CREATE VIEW FIXED_TBL_VOL_RPT AS
SELECT db
     , tbl
     , tbl_type
     , part
     , loc
     , dir_count
     , file_count
--      , loc_size
--      , locate('K', loc_size)
     , CASE
           WHEN locate('K', loc_size) > 0 THEN
               trim(substr(loc_size, 0, locate('K', loc_size) - 1)) * 1024
           WHEN locate('M', loc_size) > 0 THEN
               CAST(trim(substr(loc_size, 0, locate('M', loc_size) - 1)) * (1024 * 1024) as BIGINT)
           WHEN locate('G', loc_size) > 0 THEN
               CAST(trim(substr(loc_size, 0, locate('G', loc_size) - 1)) * (1024 * 1024 * 1024) AS BIGINT)
           ELSE
               loc_size
    END as loc_sz
FROM TBL_VOL_RPT
WHERE db is not null
  and trim(db) not in ('Database', ':---');

-- Validate Data.
SELECT distinct db
FROM FIXED_TBL_VOL_RPT
LIMIT 10;

-- Tables with Partition Count over 100 w/ avg partition size.
SELECT db,
       tbl,
       part_count,
       tbl_size,
       ROUND((tbl_size / part_count)) as avg_part_size
FROM (SELECT db
           , tbl
           , count(1) as part_count
           , ROUND(sum(loc_sz) )tbl_size
      FROM FIXED_TBL_VOL_RPT
      GROUP BY db, tbl
      HAVING part_count > 100) a;



-- Table with no partitions or less than 100 partitions w/ sizes
SELECT db,
       tbl,
       part_count,
       tbl_size,
       ROUND((tbl_size / part_count)) as avg_part_size
FROM (SELECT db
           , tbl
           , count(1) as part_count
           , ROUND(sum(loc_sz) )tbl_size
      FROM FIXED_TBL_VOL_RPT
      GROUP BY db, tbl
      HAVING part_count < 100) a
ORDER BY avg_part_size DESC;


-- Database Sizes with tbl count where there are no partitions or less than 100 partitions.
SELECT db,
       count(tbl) as tbl_count,
       sum(tbl_size) as db_size
FROM
    (SELECT db
           , tbl
           , count(1) as part_count
           , ROUND(sum(loc_sz) )tbl_size
      FROM FIXED_TBL_VOL_RPT
      GROUP BY db, tbl
      HAVING part_count < 100) a
GROUP BY db
ORDER BY db_size DESC;