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
  , SUM(VI.KILLED_TASK_COUNT) KILLED_TASK_COUNT
FROM
    HIVE_QUERY HQ
        INNER JOIN DAG_INFO DI ON HQ.ID = DI.HIVE_QUERY_ID
        INNER JOIN VERTEX_INFO VI ON DI.ID = VI.DAG_ID
WHERE
        TO_TIMESTAMP(HQ.END_TIME / 1000)::TIMESTAMPTZ >
        (CURRENT_TIMESTAMP::TIMESTAMPTZ - '${interval}'::INTERVAL)
        ${and_queue}
GROUP BY
    HQ.ID
  , HQ.REQUEST_USER
  , HQ.QUEUE_NAME
  , HQ.STATUS
--   , HQ.DATABASES_USED
--   , HQ.TABLES_READ
--   , HQ.TABLES_WRITTEN
--   , HQ.USED_CBO
--   , DI.APPLICATION_ID
--   , DI.DAG_ID
  , QUERY_START_TIME
  , QUERY_END_TIME
  , HQ.ELAPSED_TIME
  , HQ.HIVE_INSTANCE_ADDRESS
HAVING
    SUM(VI.KILLED_TASK_COUNT) > 0
ORDER BY
    KILLED_TASK_COUNT DESC
LIMIT ${limit};