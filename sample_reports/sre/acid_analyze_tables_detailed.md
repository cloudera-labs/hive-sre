# Analyze Hive ACID Tables - Detailed (v.1.8.0_265)

Post Upgrade from Hive 1/2 to Hive 3 may result in statistics that are wrong.  Actually, the statistics were wrong in Hive 1/2, but the CBO wasn't using them as it should and recalculating plans.  In Hive 3, these statistics when wrong and picked up by the CBO, could cause datasets to be disregarded.  If you are experience data issues with any tables that were ACID tables BEFORE the upgrade, you should run 'ANALYZE' on them to fix those statistics.

 *NOTE*: Markdown Renders WILL distort the commands listed.  Copy from the source file to ensure the commands are correct.
 
| DB | Table | Partitions | Hive SQL |
|:---|:---|:---|:---|
| covid_ga | county_cases_archive | year_month=RUN_1 | ANALYZE TABLE `covid_ga`.`county_cases_archive` PARTITION (year_month="RUN_1") COMPUTE STATISTICS;
| covid_ga | deaths_archive | year_month=RUN_1 | ANALYZE TABLE `covid_ga`.`deaths_archive` PARTITION (year_month="RUN_1") COMPUTE STATISTICS;
| covid_ga | demographics_archive | year_month=RUN_1 | ANALYZE TABLE `covid_ga`.`demographics_archive` PARTITION (year_month="RUN_1") COMPUTE STATISTICS;
| credit_card_01 | cc_acct_delta | year_month=2020_08 | ANALYZE TABLE `credit_card_01`.`cc_acct_delta` PARTITION (year_month="2020_08") COMPUTE STATISTICS;
| covid_github | countries_aggregated_archive | year_month=RUN_1 | ANALYZE TABLE `covid_github`.`countries_aggregated_archive` PARTITION (year_month="RUN_1") COMPUTE STATISTICS;
| covid_github | key_countries_pivoted_archive | year_month=RUN_1 | ANALYZE TABLE `covid_github`.`key_countries_pivoted_archive` PARTITION (year_month="RUN_1") COMPUTE STATISTICS;
| covid_github | reference_archive | year_month=RUN_1 | ANALYZE TABLE `covid_github`.`reference_archive` PARTITION (year_month="RUN_1") COMPUTE STATISTICS;
| covid_github | time_series_combined_archive | year_month=RUN_1 | ANALYZE TABLE `covid_github`.`time_series_combined_archive` PARTITION (year_month="RUN_1") COMPUTE STATISTICS;
| covid_github | us_confirmed_archive | year_month=RUN_1 | ANALYZE TABLE `covid_github`.`us_confirmed_archive` PARTITION (year_month="RUN_1") COMPUTE STATISTICS;
| covid_github | us_deaths_archive | year_month=RUN_1 | ANALYZE TABLE `covid_github`.`us_deaths_archive` PARTITION (year_month="RUN_1") COMPUTE STATISTICS;
| covid_github | worldwide_aggregated_archive | year_month=RUN_1 | ANALYZE TABLE `covid_github`.`worldwide_aggregated_archive` PARTITION (year_month="RUN_1") COMPUTE STATISTICS;
| priv_dstreev | catalog_sales | cs_sold_date_sk=2452447 | ANALYZE TABLE `priv_dstreev`.`catalog_sales` PARTITION (cs_sold_date_sk="2452447") COMPUTE STATISTICS;
| priv_dstreev | my_multi_part | update_dt=2009-10-23/batch=test | ANALYZE TABLE `priv_dstreev`.`my_multi_part` PARTITION (update_dt="2009-10-23",batch="test") COMPUTE STATISTICS;
| streaming_cc | cc_trans_alt_from_streaming | processing_cycle=2019-02-14 | ANALYZE TABLE `streaming_cc`.`cc_trans_alt_from_streaming` PARTITION (processing_cycle="2019-02-14") COMPUTE STATISTICS;
| streaming_cc | cc_trans_from_incremental_append | processing_cycle=2018-12-01 | ANALYZE TABLE `streaming_cc`.`cc_trans_from_incremental_append` PARTITION (processing_cycle="2018-12-01") COMPUTE STATISTICS;
| testing | multi_part | st=GA A/update_dt=2020-09-03 | ANALYZE TABLE `testing`.`multi_part` PARTITION (st="GA A",update_dt="2020-09-03") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_25 | catalog_returns | cr_returned_date_sk=2452185 | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`catalog_returns` PARTITION (cr_returned_date_sk="2452185") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_25 | catalog_sales | cs_sold_date_sk=2452221 | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`catalog_sales` PARTITION (cs_sold_date_sk="2452221") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | catalog_returns | cr_returned_date_sk=2452679 | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`catalog_returns` PARTITION (cr_returned_date_sk="2452679") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_25 | store_sales | ss_sold_date_sk=2450978 | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`store_sales` PARTITION (ss_sold_date_sk="2450978") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | catalog_returns | cr_returned_date_sk=2451258 | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`catalog_returns` PARTITION (cr_returned_date_sk="2451258") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | catalog_sales | cs_sold_date_sk=2451934 | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`catalog_sales` PARTITION (cs_sold_date_sk="2451934") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_25 | web_sales | ws_sold_date_sk=2451743 | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`web_sales` PARTITION (ws_sold_date_sk="2451743") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | catalog_sales | cs_sold_date_sk=2451657 | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`catalog_sales` PARTITION (cs_sold_date_sk="2451657") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | inventory | inv_date_sk=2451557 | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`inventory` PARTITION (inv_date_sk="2451557") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | inventory | inv_date_sk=2451858 | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`inventory` PARTITION (inv_date_sk="2451858") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | store_sales | ss_sold_date_sk=2452043 | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`store_sales` PARTITION (ss_sold_date_sk="2452043") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | store_returns | sr_returned_date_sk=__HIVE_DEFAULT_PARTITION__ | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`store_returns` PARTITION (sr_returned_date_sk="__HIVE_DEFAULT_PARTITION__") COMPUTE STATISTICS;
| tpcds_test | catalog_sales | cs_sold_date_sk=2450863 | ANALYZE TABLE `tpcds_test`.`catalog_sales` PARTITION (cs_sold_date_sk="2450863") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | web_returns | wr_returned_date_sk=2452780 | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`web_returns` PARTITION (wr_returned_date_sk="2452780") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | store_sales | ss_sold_date_sk=2452112 | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`store_sales` PARTITION (ss_sold_date_sk="2452112") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | web_returns | wr_returned_date_sk=__HIVE_DEFAULT_PARTITION__ | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`web_returns` PARTITION (wr_returned_date_sk="__HIVE_DEFAULT_PARTITION__") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | web_sales | ws_sold_date_sk=2452242 | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`web_sales` PARTITION (ws_sold_date_sk="2452242") COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | web_sales | ws_sold_date_sk=2451207 | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`web_sales` PARTITION (ws_sold_date_sk="2451207") COMPUTE STATISTICS;
