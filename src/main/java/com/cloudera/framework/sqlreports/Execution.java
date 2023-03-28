/*
 * Copyright (c) 2023. Cloudera, Inc. All Rights Reserved
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.cloudera.framework.sqlreports;

import com.cloudera.framework.sqlreports.definition.Item;
import com.cloudera.framework.sqlreports.navigation.Option;
import com.cloudera.framework.sqlreports.navigation.Scenario;
import com.cloudera.framework.sqlreports.navigation.Score;
import com.cloudera.framework.sqlreports.navigation.WeightedOption;
import com.cloudera.utils.common.TokenReplacement;
import com.cloudera.utils.sql.ResultArray;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;


public class Execution {
    private static Logger LOG = LogManager.getLogger(Execution.class);

    private DataSource datasource = null;

    public Execution(DataSource datasource) {
        this.datasource = datasource;
    }

    /*
    The last scenario will provide overrides for some of the default parameters so you align
    with any previous
     */
//    public ResultArray run(Item item, Map<String, Object> record, Scenario lastScenario) {
//
//    }

    public ResultArray run(Item item, Map<String, Object> record, Scenario lastScenario) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        ResultArray rtn = null;
        try {
            conn = datasource.getConnection();
            Map<String, Object> defaultParams = item.getQuery().getParameters();

            List<String> queryStrList = item.getQuery().getQuery();
            // Collect to parameters into a consolidated map.
            Map<String, Object> cMap = condense(defaultParams, record, lastScenario.getParameters());

            List<String> query = TokenReplacement.getInstance().replace(queryStrList, cMap, Boolean.TRUE);
            String queryStr = query.stream().collect(Collectors.joining("\n"));
            LOG.info("Execution Query: " + queryStr);
            statement = conn.prepareStatement(queryStr);

            rs = statement.executeQuery();
            rtn = new ResultArray(rs);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            if (rs != null)
                rs.close();
            if (statement != null)
                statement.close();
            if (conn != null)
                conn.close();
        }
        return rtn;
    }

    public ResultArray run(Option option, Map<String, Object> scenarioParams) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        ResultArray rtn = null;
        try {
            conn = datasource.getConnection();
            Item item = option.getQueryItem();
            Map<String, Object> defaultParams = item.getQuery().getParameters();
//            Map<String, Object> scenarioParams = scenario.getParameters();

            List<String> queryStrList = item.getQuery().getQuery();
            // Collect to parameters into a consolidated map.
            Map<String, Object> cMap = condense(defaultParams, scenarioParams);

            List<String> query = TokenReplacement.getInstance().replace(queryStrList, cMap, Boolean.TRUE);
            String queryStr = query.stream().collect(Collectors.joining("\n"));
            LOG.info("Execution Query: " + queryStr);
            statement = conn.prepareStatement(queryStr);

            rs = statement.executeQuery();
            rtn = new ResultArray(rs);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            if (rs != null)
                rs.close();
            if (statement != null)
                statement.close();
            if (conn != null)
                conn.close();
        }
        return rtn;
    }

    public ResultArray runScoreRanking(Score score, String keyField, List<String> display,
                                       Map<String, Object> scenarioParams, Integer resultLimit) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        ResultArray rtn = null;

//        ResultArray[] weights = new ResultArray[score.getScored().size()];
        Map<WeightedOption, ResultArray> weights = new HashMap<WeightedOption, ResultArray>();
        try {
            conn = datasource.getConnection();
            for (WeightedOption option : score.getScored()) {
                Item item = option.getQueryItem();
                Map<String, Object> defaultParams = item.getQuery().getParameters();

                List<String> queryStrList = item.getQuery().getQuery();
                // Collect to parameters into a consolidated map.
                Map<String, Object> cMap = condense(defaultParams, scenarioParams);

                List<String> query = TokenReplacement.getInstance().replace(queryStrList, cMap, Boolean.TRUE);
                String queryStr = query.stream().collect(Collectors.joining("\n"));
                LOG.info("Weighted Option: " + option.getShortDesc() + " Execution Query: " + queryStr);
                statement = conn.prepareStatement(queryStr);

                rs = statement.executeQuery();
                weights.put(option, new ResultArray(rs));
            }

            // TODO: Go through the weights and build a final ResultArray.
            rtn = rank(weights, keyField, display, resultLimit);

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            if (rs != null)
                rs.close();
            if (statement != null)
                statement.close();
            if (conn != null)
                conn.close();
        }
        return rtn;
    }



    protected ResultArray rank(Map<WeightedOption, ResultArray> rankSource, String keyField, List<String> displayFields,
                               Integer resultLimit) {
        // Build a map that will be populated and updated as we loop through the weighted rank results.
        Map<String, RankedQuery> rankedQueryMap = new HashMap<String, RankedQuery>();
        for (WeightedOption wo : rankSource.keySet()) {
            ResultArray raCheck = rankSource.get(wo);
            int recordCount = raCheck.getRecords().size(); // number of records
            int recordWeight = wo.getWeight();
            for (int i = 0; i < recordCount; i++) {
                Map<String, Object> record = raCheck.getRecord(i);
                // Get the key.
                String value = record.get(keyField).toString();
                // Lookup the value in the rankedQueryMap.
                RankedQuery rq = rankedQueryMap.get(value);
                if (rq == null) {
                    // build
                    rq = new RankedQuery();
                    rq.key = value;
                    for (String displayKey: displayFields) {
                        rq.values.add(record.get(displayKey));
                    }
                    rankedQueryMap.put(value, rq);
                }
                rq.rank += ((recordCount - i) * recordWeight);
            }
        }
        // Now Sort the list and build the return ResultArray.
        List<RankedQuery> sortedRankedQuery = new ArrayList<RankedQuery>(rankedQueryMap.values());
        sortedRankedQuery.sort(new RankedQueryComparator());

        ResultArray rtn = RankedQuery.buildResultArray(sortedRankedQuery, keyField, displayFields, resultLimit);

        return rtn;
    }

    /*
    Take in an array of maps and consolidate them into a condensed hierarchy of overriding values.
     */
    protected Map<String, Object> condense(Map<String, Object>... mapSet) {
        Map<String, Object> rtn = new HashMap<String, Object>();
        for (Map<String, Object> map : mapSet) {
            for (String key : map.keySet()) {
                rtn.put(key, map.get(key));
            }
        }
        return rtn;
    }

    public Boolean validateConnection() {


        return Boolean.TRUE;
    }
}
