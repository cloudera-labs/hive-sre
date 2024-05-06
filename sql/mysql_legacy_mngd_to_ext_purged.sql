-- 0. Look for MANAGED non-transactional tables. Test after process too.  Should be empty.
SELECT
    D.NAME
  , T.TBL_NAME
  , DATE_FORMAT(FROM_UNIXTIME(T.CREATE_TIME), '%Y-%m-%d') AS CREATED_DATE
  , T.TBL_TYPE
  , S.LOCATION
FROM TBLS T
         INNER JOIN DBS D ON T.DB_ID = D.DB_ID
         INNER JOIN SDS S ON T.SD_ID = S.SD_ID
WHERE
        T.TBL_ID NOT IN (
                        SELECT
                            T2.TBL_ID
                        FROM TABLE_PARAMS TP
                                 INNER JOIN TBLS T2 ON TP.TBL_ID = T2.TBL_ID
                        WHERE
                            TP.PARAM_KEY = 'transactional'
                          AND BINARY TP.PARAM_VALUE = 'true') -- case insensitive equality check
  AND TBL_TYPE = 'MANAGED_TABLE'
ORDER BY D.NAME, T.TBL_NAME;


BEGIN;

-- Count external true;
SELECT
    COUNT(1)
FROM TABLE_PARAMS
WHERE
      PARAM_KEY = 'EXTERNAL'
  AND BINARY PARAM_VALUE = 'TRUE';

COMMIT;
ROLLBACK;

-- 1. table params add EXTERNAL property
INSERT INTO TABLE_PARAMS
SELECT
    T.TBL_ID
  , 'EXTERNAL'
  , 'TRUE'
FROM TBLS T
         INNER JOIN DBS D ON T.DB_ID = D.DB_ID
WHERE
        T.TBL_ID NOT IN (
                        SELECT
                            T2.TBL_ID
                        FROM TABLE_PARAMS TP
                                 INNER JOIN TBLS T2 ON TP.TBL_ID = T2.TBL_ID
                        WHERE
                            TP.PARAM_KEY = 'transactional'
                          AND BINARY TP.PARAM_VALUE = 'true') -- case insensitive equality check
  AND TBL_TYPE = 'MANAGED_TABLE'
ON DUPLICATE KEY UPDATE PARAM_VALUE = 'FALSE';

-- 2. table params add external purge flag
INSERT INTO TABLE_PARAMS
SELECT
    T.TBL_ID
  , 'external.table.purge'
  , 'TRUE'
FROM TBLS T
         INNER JOIN DBS D ON T.DB_ID = D.DB_ID
WHERE
        T.TBL_ID NOT IN (
                        SELECT
                            T2.TBL_ID
                        FROM TABLE_PARAMS TP
                                 INNER JOIN TBLS T2 ON TP.TBL_ID = T2.TBL_ID
                        WHERE
                            TP.PARAM_KEY = 'transactional'
                          AND BINARY TP.PARAM_VALUE = 'true') -- case insensitive equality check
  AND TBL_TYPE = 'MANAGED_TABLE'
ON DUPLICATE KEY UPDATE PARAM_VALUE = 'TRUE';

-- 3. tbls change table type.
UPDATE TBLS
SET TBL_TYPE = 'EXTERNAL_TABLE'
WHERE
        TBL_ID IN (
                  SELECT
                      T2.TBL_ID
                  FROM (
                       SELECT *
                       FROM TBLS) T2
                           INNER JOIN DBS D ON T2.DB_ID = D.DB_ID
                  WHERE
                          T2.TBL_ID NOT IN (
                                           SELECT
                                               T3.TBL_ID
                                           FROM TABLE_PARAMS TP
                                                    INNER JOIN (
                                                               SELECT *
                                                               FROM TBLS) T3 ON TP.TBL_ID = T3.TBL_ID
                                           WHERE
                                               TP.PARAM_KEY = 'transactional'
                                             AND BINARY TP.PARAM_VALUE = 'true') -- case insensitive equality check
                    AND TBL_TYPE = 'MANAGED_TABLE');


-- Bucketing Version
INSERT INTO TABLE_PARAMS
SELECT
    T2.TBL_ID
  , 'bucketing_version'
  , '2'
FROM ((
      SELECT DISTINCT
          DB.NAME AS DB_NAME
        , TBL.TBL_NAME
        , TBL.TBL_ID
      FROM DBS DB
               JOIN
           TBLS TBL ON
               DB.DB_ID = TBL.DB_ID
               INNER JOIN SDS S ON TBL.SD_ID = S.SD_ID
               INNER JOIN (
                          SELECT
                              SP.TBL_ID
                            , SP.PARAM_KEY
                            , SP.PARAM_VALUE
                          FROM TABLE_PARAMS SP
                          WHERE
                                LOWER(SP.PARAM_KEY) = 'transactional'
                            AND LOWER(SP.PARAM_VALUE) = 'true') P
                          ON TBL.TBL_ID = P.TBL_ID) WCAT WHERE
        WCAT.TBL_ID NOT IN (
                           SELECT
                               TBL_ID
                           FROM (
                                SELECT
                                    SP.TBL_ID
                                  , SP.PARAM_KEY
                                  , SP.PARAM_VALUE
                                FROM TABLE_PARAMS SP
                                WHERE
                                      LOWER(SP.PARAM_KEY) = 'bucketing_version'
                                  AND SP.PARAM_VALUE = '2'
                                ) BPARAMS)) T2
ON DUPLICATE KEY UPDATE PARAM_VALUE = '2';

INSERT INTO TABLE_PARAMS
SELECT
  TBL_ID
, 'bucketing_version'
, '2'
FROM (
     SELECT DISTINCT
         DB.NAME AS DB_NAME
       , TBL.TBL_NAME
       , TBL.TBL_ID
     FROM DBS DB
              JOIN
          TBLS TBL ON
              DB.DB_ID = TBL.DB_ID
              INNER JOIN SDS S ON TBL.SD_ID = S.SD_ID
              INNER JOIN (
                         SELECT
                             SP.TBL_ID
                           , SP.PARAM_KEY
                           , SP.PARAM_VALUE
                         FROM TABLE_PARAMS SP
                         WHERE
                               LOWER(SP.PARAM_KEY) = 'transactional'
                           AND LOWER(SP.PARAM_VALUE) = 'true'
     ) P
                         ON TBL.TBL_ID = P.TBL_ID
     ) WCAT
WHERE
        WCAT.TBL_ID NOT IN (
                           SELECT
                               TBL_ID
                           FROM (
                                SELECT
                                    SP.TBL_ID
                                  , SP.PARAM_KEY
                                  , SP.PARAM_VALUE
                                FROM TABLE_PARAMS SP
                                WHERE
                                      LOWER(SP.PARAM_KEY) = 'bucketing_version'
                                  AND SP.PARAM_VALUE = '2'
                                ) BPARAMS)
ON DUPLICATE KEY UPDATE PARAM_VALUE = '2';


