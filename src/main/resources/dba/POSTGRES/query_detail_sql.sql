SELECT
    HQ.ID
  , HQ.QUERY
--   , HQ.REQUEST_USER
--   , HQ.DATABASES_USED
--   , HQ.TABLES_READ
--   , HQ.TABLES_WRITTEN
FROM
    HIVE_QUERY HQ
WHERE
    HQ.ID = ${id}