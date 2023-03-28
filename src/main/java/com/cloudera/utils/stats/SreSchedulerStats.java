package com.cloudera.utils.stats;

import com.cloudera.utils.hadoop.yarn.SchedulerStats;

import java.util.List;
import java.util.Map;

public class SreSchedulerStats extends SreStats {

    public SreSchedulerStats() {
        super();
        setStats(new SchedulerStats(getCli().getEnv().getConfig()));
    }

    @Override
    public void run() {
        getStats().execute();
        List<Map<String, Object>> results = getStats().getRecordList(SchedulerStats.QUEUE);
        // Save to DB
        print(getStats().getRecordFieldMap().get(SchedulerStats.QUEUE), results);

        results = getStats().getRecordList(SchedulerStats.QUEUE_USAGE);
        // Save to DB
        print(getStats().getRecordFieldMap().get(SchedulerStats.QUEUE_USAGE), results);
        getStats().clearCache();
    }

}
