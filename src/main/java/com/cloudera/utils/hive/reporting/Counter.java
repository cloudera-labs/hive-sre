package com.cloudera.utils.hive.reporting;

import java.util.List;

public interface Counter {

    String getDisplayName();

    ReportCounter getCounter();
    void setCounter(ReportCounter reportCounter);

    int getStatus();

    String getStatusStr();

    List<ReportCounter> getCounterChildren();

    void setStatus(int status);

    void incProcessed(int increment);

    void setTotalCount(long totalCount);

    void incSuccess(int increment);

    void incError(int increment);

}
