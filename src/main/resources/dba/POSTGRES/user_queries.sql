SELECT
    HQ.ID
  , HQ.REQUEST_USER
  , HQ.QUEUE_NAME
  , HQ.STATUS
--   , HQ.DATABASES_USED
--   , HQ.TABLES_READ
--   , HQ.TABLES_WRITTEN
--   , HQ.USED_CBO
  , DI.APPLICATION_ID
--   , DI.DAG_ID
  , TO_TIMESTAMP(HQ.START_TIME / 1000)::TIMESTAMP(0) QUERY_START_TIME
  , TO_TIMESTAMP(HQ.END_TIME / 1000)::TIMESTAMP(0)   QUERY_END_TIME
  , HQ.HIVE_INSTANCE_ADDRESS                         SERVER_INSTANCE
  , HQ.ELAPSED_TIME
  , HQ.CPU_TIME
FROM
    HIVE_QUERY HQ
--         INNER JOIN DAG_INFO DI ON HQ.ID = DI.HIVE_QUERY_ID
WHERE
        TO_TIMESTAMP(HQ.END_TIME / 1000)::TIMESTAMPTZ >
        (CURRENT_TIMESTAMP::TIMESTAMPTZ - '${interval}':: INTERVAL)
  AND   HQ.REQUEST_USER = '${request_user}'
    ${and_queue}
ORDER BY
    QUERY_START_TIME DESC
LIMIT ${limit}