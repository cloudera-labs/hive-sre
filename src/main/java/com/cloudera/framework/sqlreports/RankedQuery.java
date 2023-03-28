package com.cloudera.framework.sqlreports;

import com.cloudera.utils.sql.ResultArray;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.sql.Types.VARCHAR;

public class RankedQuery {

    int rank = 0;
    String key = null;
    List<Object> values = new ArrayList<Object>();

    public static ResultArray buildResultArray(List<RankedQuery> rankedQueries, String keyField, List<String> displayFields,
                                               Integer resultLimit) {
        ResultArray rtn = new ResultArray();
        RankedQuery sample = rankedQueries.get(0);
        if (rankedQueries != null && rankedQueries.size() > 0) {
            rtn.setColumnCount(sample.values.size() + 2);
            rtn.getFields().clear(); // ensure this is empty before we start.
            rtn.getFields().put("rank", Types.BIGINT);
            rtn.getFields().put(keyField, VARCHAR);
            for (String field : displayFields) {
                rtn.getFields().put(field, VARCHAR);
            }
        } else {
            return rtn;
        }
        try {

            List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(rtn.getFields().entrySet());
            Map.Entry<String, Integer> lastEntry = entryList.get(entryList.size() - 1);

            String lastField = lastEntry.getKey();
            int limit = 1;
            for (RankedQuery rankedQuery : rankedQueries) {
                Object[] record = new Object[rtn.getColumnCount()];
                int i = 0;
                record[i++] = rankedQuery.rank;
                record[i++] = rankedQuery.key;
                for (Object value : rankedQuery.values) {
                    record[i++] = value.toString();
                }
                rtn.getRecords().add(record);
                if (limit++ >= resultLimit) {
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return rtn;
    }

}
