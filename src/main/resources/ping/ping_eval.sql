USE ${PING_DB};

-- Validate
SELECT *
FROM
    PING
LIMIT 10;

-- General Latency Report
SELECT
    BATCH_ID
  , SOURCE_HOST
  , TARGET_HOST
  , COUNT(1)               ATTEMPTS
  , ROUND(AVG(LETANCY), 3) AVG_LETANCY
  , MIN(LETANCY)           MIN_LETANCY
  , MAX(LETANCY)           MAX_LETANCY
FROM
    PING
WHERE
      STATUS = 1
  AND BATCH_ID = "${BATCH_ID}"
GROUP BY
    BATCH_ID, SOURCE_HOST, TARGET_HOST
;

-- MISSING SOURCE - means that the ping attempt wasn't done FROM these hosts
--      Either a mapper didn't run on the host OR the host
--          wasn't a YARN Compute Node OR
--          queue placement / rules / partitioning didn't allow
--          job to run on the host.
-- MISSING TARGET - means the host wasn't in the HOST LIST but was a Compute Node.
-- Missing Connection Attempts
WITH TARGET_HOST_LIST AS (
    SELECT DISTINCT
        TARGET_HOST HOST
    FROM
        PING
    WHERE
        BATCH_ID = "${BATCH_ID}"
),
     SOURCE_HOST_LIST AS (
         SELECT DISTINCT
             SOURCE_HOST HOST
         FROM
             PING
         WHERE
             BATCH_ID = "${BATCH_ID}"
     )
SELECT DISTINCT
    "MISSING SOURCE" SCENARIO, THL.HOST
FROM
    TARGET_HOST_LIST THL
WHERE
        THL.HOST NOT IN (
        SELECT
            HOST
        FROM
            SOURCE_HOST_LIST)
UNION ALL
SELECT DISTINCT
    "MISSING TARGET" SCENARIO, SHL.HOST
FROM
    SOURCE_HOST_LIST SHL
WHERE
        SHL.HOST NOT IN (
        SELECT
            HOST
        FROM
            TARGET_HOST_LIST);

-- Look for Problems
SELECT
    BATCH_ID
  , SOURCE_HOST
  , TARGET_HOST
  , MIN(TIME_STAMP)        MIN_TIMESTAMP
  , MAX(TIME_STAMP)        MAX_TIMESTAMP
  , COUNT(1)               ATTEMPTS
  , ROUND(AVG(LETANCY), 3) AVG_LETANCY
  , MIN(LETANCY)           MIN_LETANCY
  , MAX(LETANCY)           MAX_LETANCY
FROM
    PING
WHERE
      STATUS = 1
  AND BATCH_ID = "${BATCH_ID}"
GROUP BY
    BATCH_ID, SOURCE_HOST, TARGET_HOST
HAVING
     ROUND(AVG(LETANCY), 3) > 1
  OR MAX_LETANCY > 3
;

-- Bad Ping attempts
-- Route between hosts is bad or an error happened.
-- Target Host may not be resolvable.
-- Check by logging on to SOURCE HOST and ping TARGET.
SELECT
    DISTINCT
    BATCH_ID
  , SOURCE_HOST
  , TARGET_HOST
  , CASE STATUS
    WHEN -1 THEN "UNREACHABLE"
    WHEN -2 THEN "ERROR"
    ELSE "UNKNOWN"
  END STATUS_STR
FROM
    PING
WHERE
      STATUS != 1
  AND BATCH_ID = "${BATCH_ID}"
;
