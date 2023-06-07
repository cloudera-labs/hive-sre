WITH
    SUB AS (SELECT
                HQ.ID
              , HQ.REQUEST_USER
              , HQ.QUEUE_NAME
              , HQ.STATUS
--               , HQ.DATABASES_USED
--               , HQ.TABLES_READ
--               , HQ.TABLES_WRITTEN
--               , HQ.USED_CBO
              , DI.APPLICATION_ID
--               , DI.DAG_ID
              , TO_TIMESTAMP(HQ.START_TIME / 1000)::TIMESTAMP(0)            QUERY_START_TIME
              , TO_TIMESTAMP(HQ.END_TIME / 1000)::TIMESTAMP(0)              QUERY_END_TIME
              , HQ.HIVE_INSTANCE_ADDRESS                                    SERVER_INSTANCE
              , JSON_EXTRACT_PATH(QD.PERF::JSON, '${perf}') ::TEXT::DECIMAL PERF_TIME_MS
            FROM
                HIVE_QUERY HQ
                    INNER JOIN DAG_INFO DI ON HQ.ID = DI.HIVE_QUERY_ID
                    INNER JOIN QUERY_DETAILS QD ON HQ.ID = QD.HIVE_QUERY_ID
            WHERE
                    TO_TIMESTAMP(HQ.END_TIME / 1000)::TIMESTAMPTZ >
                    (CURRENT_TIMESTAMP::TIMESTAMPTZ - '${interval}':: INTERVAL)
                ${and_queue}
            )
SELECT *
FROM
    SUB
ORDER BY
    PERF_TIME_MS DESC
LIMIT ${limit};