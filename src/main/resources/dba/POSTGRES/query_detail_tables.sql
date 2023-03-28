WITH
    SUB AS (SELECT
                HQ.ID
--               , HQ.REQUEST_USER
--               , HQ.QUEUE_NAME
--               , HQ.STATUS
--               , HQ.DATABASES_USED
              , JSON_ARRAY_ELEMENTS(HQ.TABLES_READ::JSON)    TABLE_READ
              , JSON_ARRAY_ELEMENTS(HQ.TABLES_WRITTEN::JSON) TABLE_WRITTEN
--               , TO_TIMESTAMP(HQ.START_TIME / 1000)::TIMESTAMP(0) QUERY_START_TIME
--               , TO_TIMESTAMP(HQ.END_TIME / 1000)::TIMESTAMP(0)   QUERY_END_TIME
--               , HQ.HIVE_INSTANCE_ADDRESS SERVER_INSTANCE
            FROM
                HIVE_QUERY HQ
--                     INNER JOIN DAG_INFO DI ON HQ.ID = DI.HIVE_QUERY_ID
            WHERE
                HQ.ID = ${id})
  , LISTED AS (SELECT
                   ID
--                  , REQUEST_USER
--                  , QUEUE_NAME
--                  , STATUS
--                  , DATABASES_USED
                 , CONCAT(JSON_EXTRACT_PATH(TABLE_READ::JSON, 'database'), '.',
                          JSON_EXTRACT_PATH(TABLE_READ::JSON, 'table'))    TBL_READ
                 , CONCAT(JSON_EXTRACT_PATH(TABLE_WRITTEN::JSON, 'database'), '.',
                          JSON_EXTRACT_PATH(TABLE_WRITTEN::JSON, 'table')) TBL_WRITTEN
--                  , QUERY_START_TIME
--                  , QUERY_END_TIME
--                  , SERVER_INSTANCE
               FROM
                   SUB)
SELECT
    *
FROM
    LISTED
