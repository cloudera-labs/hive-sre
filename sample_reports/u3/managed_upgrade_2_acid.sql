Hive 3 Upgrade Checks - Managed Non-ACID to ACID Table Migrations
-- Table is owned by 'dstreev', not 'hive', and NOT currently ACID.
 -- This table 'could' be migrated to an ACID table unless changed.
 -- Recommend forcing the manual conversion to ensure table isn't inadvertently migrated.
 ALTER TABLE default.test1 SET TBLPROPERTIES('EXTERNAL'='TRUE', 'external.table.purge'='true');
