package com.cloudera.utils.stats;

import com.cloudera.utils.hadoop.yarn.ContainerStats;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SreContainerStats extends SreStats {

    public SreContainerStats() {
        super();
        setStats(new ContainerStats(getCli().getEnv().getConfig()));
    }

    @Override
    public void run() {
        getStats().execute();
        List<Map<String, Object>> results = getStats().getRecordList(ContainerStats.APP);
        // Save to DB
        print(getStats().getRecordFieldMap().get(ContainerStats.APP), results);
        getStats().clearCache();
    }

}
