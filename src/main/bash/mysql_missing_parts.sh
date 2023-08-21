#! /bin/bash

# Build Mysql Statements for Metastore to remove partitions that didn't have directories in HDFS.

TMPDIR=$(mktemp -d)

echo "Temp Dir: $TMPDIR"

$(dirname $0)/get_part_ids.sh $1 $TMPDIR/part_ids.txt

while IFS="|" read -r line
do
  echo "Line: $line"
  # PART_COL_PRIVS
  echo "DELETE FROM PART_COL_PRIVS WHERE PART_ID IN ($line);" >> mysql_missing_parts.sql
  # PART_COL_STATS
  echo "DELETE FROM PART_COL_STATS WHERE PART_ID IN ($line);" >> mysql_missing_parts.sql
  # PART_PRIVS
  echo "DELETE FROM PART_PRIVS WHERE PART_ID IN ($line);" >> mysql_missing_parts.sql
  # PARTITION_KEY_VALS
  echo "DELETE FROM PARTITION_KEY_VALS WHERE PART_ID IN ($line);" >> mysql_missing_parts.sql
  # PARTITION_PARAMS
  echo "DELETE FROM PARTITION_PARAMS WHERE PART_ID IN ($line);" >> mysql_missing_parts.sql
  # PARTITIONS
  echo "DELETE FROM PARTITIONS WHERE PART_ID IN ($line);" >> mysql_missing_parts.sql
done <$TMPDIR/part_ids.txt
