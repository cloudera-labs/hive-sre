-- Copyright 2021 Cloudera, Inc. All Rights Reserved.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

Hive 3 Upgrade Checks - Managed Non-ACID to ACID Table Migrations
-- Table is owned by 'dstreev', not 'hive', and NOT currently ACID.
 -- This table 'could' be migrated to an ACID table unless changed.
 -- Recommend forcing the manual conversion to ensure table isn't inadvertently migrated.
 ALTER TABLE default.test1 SET TBLPROPERTIES('EXTERNAL'='TRUE', 'external.table.purge'='true');
