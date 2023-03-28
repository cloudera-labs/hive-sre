WITH
    SUB AS (SELECT
                HQ.ID
              , HQ.REQUEST_USER
              , HQ.QUEUE_NAME
              , HQ.STATUS
              , HQ.DATABASES_USED
              , JSON_ARRAY_ELEMENTS(HQ.TABLES_WRITTEN::JSON) TABLE_WRITTEN
              , TO_TIMESTAMP(HQ.START_TIME / 1000)::TIMESTAMP(0) QUERY_START_TIME
              , TO_TIMESTAMP(HQ.END_TIME / 1000)::TIMESTAMP(0)   QUERY_END_TIME
              , HQ.HIVE_INSTANCE_ADDRESS SERVER_INSTANCE
            FROM
                HIVE_QUERY HQ
                    INNER JOIN DAG_INFO DI ON HQ.ID = DI.HIVE_QUERY_ID
            WHERE
                TO_TIMESTAMP(HQ.END_TIME / 1000)::TIMESTAMPTZ >
                    (CURRENT_TIMESTAMP::TIMESTAMPTZ - '${interval}':: INTERVAL)
                ${and_queue}
            )
  , LISTED AS (SELECT
                   ID
                 , REQUEST_USER
                 , QUEUE_NAME
                 , STATUS
                 , DATABASES_USED
                 , CONCAT(JSON_EXTRACT_PATH(TABLE_WRITTEN::JSON, 'database'), '.',
                          JSON_EXTRACT_PATH(TABLE_WRITTEN::JSON, 'table')) TBL
                 , QUERY_START_TIME
                 , QUERY_END_TIME
                 , SERVER_INSTANCE
               FROM
                   SUB)
SELECT
    TBL
  , REQUEST_USER
  , COUNT(1) USED_COUNT
FROM
    LISTED
GROUP BY
    TBL, REQUEST_USER
ORDER BY
    USED_COUNT DESC
LIMIT ${limit};