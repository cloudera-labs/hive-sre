package com.cloudera.framework.sqlreports;

import java.util.Comparator;

public class RankedQueryComparator implements Comparator<RankedQuery> {
    @Override
    public int compare(RankedQuery o1, RankedQuery o2) {
        if (o1.rank < o2.rank)
            return 1;
        else if (o1.rank == o2.rank)
            return 0;
        else
            return -1;
    }
}
