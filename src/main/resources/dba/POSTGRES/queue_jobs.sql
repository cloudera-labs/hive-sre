/*
 Find queues that had queries backed up in them.  Broken down by the 'hour'.
  - Look for Startup Delays..

 Also need option to list queries in a queue, during that hour timeframe.

 */
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
  , TO_TIMESTAMP(HQ.START_TIME / 1000)::TIMESTAMP(0) QUERY_START_TIME
  , TO_TIMESTAMP(HQ.END_TIME / 1000)::TIMESTAMP(0)   QUERY_END_TIME
  , HQ.HIVE_INSTANCE_ADDRESS                         SERVER_INSTANCE
  , HQ.ELAPSED_TIME                                  ELAPSED_TIME
  , HQ.WAITING_TIME                                  WAITING_TIME
  , HQ.CPU_TIME
FROM
    HIVE_QUERY HQ
        INNER JOIN DAG_INFO DI ON HQ.ID = DI.HIVE_QUERY_ID
WHERE
    TO_TIMESTAMP(HQ.START_TIME / 1000)::TIMESTAMPTZ BETWEEN
        to_timestamp('${start_hour}', 'YYYY-MM-DD HH24:00') AND to_timestamp('${start_hour}', 'YYYY-MM-DD HH24:00') + '1 day'::INTERVAL
    AND HQ.QUEUE_NAME = '${queue_name}'
ORDER BY
    ${order_by}
