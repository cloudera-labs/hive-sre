/*
 Find queues that had queries backed up in them.  Broken down by the 'hour'.
  - Look for Startup Delays..

 Also need option to list queries in a queue, during that hour timeframe.

 */
SELECT
    HQ.QUEUE_NAME                                                                                    QUEUE_NAME
  , to_char(date_trunc('hour', TO_TIMESTAMP(HQ.START_TIME / 1000)::TIMESTAMP), 'YYYY-MM-DD HH24:00') START_HOUR
  , HQ.HIVE_INSTANCE_ADDRESS                                                                         SERVER_INSTANCE
  , ROUND(AVG(HQ.WAITING_TIME), 0)                                                                   AVG_WAIT_OVER_THRESHOLD_MS
  , MAX(HQ.WAITING_TIME)                                                                             MAX_WAIT_MS
  , count(1)                                                                                         OVER_THRESHOLD_COUNT
FROM
    HIVE_QUERY HQ
WHERE
      TO_TIMESTAMP(HQ.END_TIME / 1000)::TIMESTAMPTZ >
        (CURRENT_TIMESTAMP::TIMESTAMPTZ - '${interval}':: INTERVAL)
  AND HQ.WAITING_TIME
    > ${wait_threshold}
GROUP BY
    QUEUE_NAME
        , START_HOUR
        , SERVER_INSTANCE
ORDER BY
    OVER_THRESHOLD_COUNT
DESC
