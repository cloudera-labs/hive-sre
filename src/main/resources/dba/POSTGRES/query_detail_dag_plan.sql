SELECT
    HQ.ID
--   , HQ.QUERY
--   , HQ.REQUEST_USER
--   , HQ.DATABASES_USED
--   , HQ.TABLES_READ
--   , HQ.TABLES_WRITTEN
  , DD.DAG_PLAN_COMPRESSED
FROM
    HIVE_QUERY HQ
        INNER JOIN DAG_DETAILS DD ON HQ.ID = DD.HIVE_QUERY_ID
WHERE
    HQ.ID = ${id}