#! /bin/bash
PARTIDS=()

# Parse the loc missing dirs file for the partition id.
# Build a list of id's that can be added to an 'in' clause
# in SQL to modify the need tables.

while IFS="|" read -r one two three four five six
do
  nospaces=${six// } # remove leading spaces

  re='^[0-9]+$'
  if  [[ $nospaces =~ $re ]] ; then
     # echo "$nospaces"
     PARTIDS+=("$nospaces")
  fi
done < <(grep ^\| $1)

arraylength=${#PARTIDS[@]}
#echo "Total Partition Ids: $arraylength"

# Set the initial item.
PARTS_LIST=${PARTIDS[0]}
for (( i=1; i<${arraylength}; i++ ));
do
  # New Line every 10000 records.
  if [ `expr $i % 10000` -eq 0 ];
  then
    # Print the list so far.
    echo "${PARTS_LIST}" >> $2
    # Reset the list
    PARTS_LIST=${PARTIDS[$i]}
  else
    PARTS_LIST="$PARTS_LIST,${PARTIDS[$i]}"
  fi
done

# Print the list
echo "${PARTS_LIST}" >> $2