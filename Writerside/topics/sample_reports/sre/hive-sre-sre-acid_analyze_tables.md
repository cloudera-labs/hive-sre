# Hive ACID Tables (v.2.4.0.21.0-SNAPSHOT)

Post Upgrade from Hive 1/2 to Hive 3 may result in statistics that are wrong.  Actually, the statistics were wrong in Hive 1/2, but the CBO wasn't using them as it should and recalculating plans.  In Hive 3, these statistics when wrong and picked up by the CBO, could cause datasets to be disregarded.  If you are experience data issues with any tables that were ACID tables BEFORE the upgrade, you should run 'ANALYZE' on them to fix those statistics.
 *NOTE*: Markdown Renders WILL distort the commands listed.  Copy from the source file to ensure the commands are correct.
 
| DB | Table | Partitions | Hive SQL |
|:---|:---|:---|:---|
| covid_ga | county_cases |   | ANALYZE TABLE `covid_ga`.`county_cases` COMPUTE STATISTICS;|
| covid_ga | county_cases_archive | year_month | ANALYZE TABLE `covid_ga`.`county_cases_archive` PARTITION (year_month) COMPUTE STATISTICS;
| covid_ga | deaths |   | ANALYZE TABLE `covid_ga`.`deaths` COMPUTE STATISTICS;|
| covid_ga | deaths_archive | year_month | ANALYZE TABLE `covid_ga`.`deaths_archive` PARTITION (year_month) COMPUTE STATISTICS;
| covid_ga | demographics |   | ANALYZE TABLE `covid_ga`.`demographics` COMPUTE STATISTICS;|
| covid_ga | demographics_archive | year_month | ANALYZE TABLE `covid_ga`.`demographics_archive` PARTITION (year_month) COMPUTE STATISTICS;
| credit_card_01 | cc_acct |   | ANALYZE TABLE `credit_card_01`.`cc_acct` COMPUTE STATISTICS;|
| credit_card_01 | cc_acct_delta | year_month | ANALYZE TABLE `credit_card_01`.`cc_acct_delta` PARTITION (year_month) COMPUTE STATISTICS;
| credit_card_01 | cc_trans | year_month | ANALYZE TABLE `credit_card_01`.`cc_trans` PARTITION (year_month) COMPUTE STATISTICS;
| covid_github | countries_aggregated_archive | year_month | ANALYZE TABLE `covid_github`.`countries_aggregated_archive` PARTITION (year_month) COMPUTE STATISTICS;
| covid_github | key_countries_pivoted |   | ANALYZE TABLE `covid_github`.`key_countries_pivoted` COMPUTE STATISTICS;|
| covid_github | key_countries_pivoted_archive | year_month | ANALYZE TABLE `covid_github`.`key_countries_pivoted_archive` PARTITION (year_month) COMPUTE STATISTICS;
| covid_github | reference |   | ANALYZE TABLE `covid_github`.`reference` COMPUTE STATISTICS;|
| covid_github | reference_archive | year_month | ANALYZE TABLE `covid_github`.`reference_archive` PARTITION (year_month) COMPUTE STATISTICS;
| covid_github | time_series_combined |   | ANALYZE TABLE `covid_github`.`time_series_combined` COMPUTE STATISTICS;|
| covid_github | time_series_combined_archive | year_month | ANALYZE TABLE `covid_github`.`time_series_combined_archive` PARTITION (year_month) COMPUTE STATISTICS;
| covid_github | us_confirmed |   | ANALYZE TABLE `covid_github`.`us_confirmed` COMPUTE STATISTICS;|
| covid_github | us_confirmed_archive | year_month | ANALYZE TABLE `covid_github`.`us_confirmed_archive` PARTITION (year_month) COMPUTE STATISTICS;
| covid_github | us_deaths |   | ANALYZE TABLE `covid_github`.`us_deaths` COMPUTE STATISTICS;|
| covid_github | us_deaths_archive | year_month | ANALYZE TABLE `covid_github`.`us_deaths_archive` PARTITION (year_month) COMPUTE STATISTICS;
| covid_github | worldwide_aggregated |   | ANALYZE TABLE `covid_github`.`worldwide_aggregated` COMPUTE STATISTICS;|
| covid_github | worldwide_aggregated_archive | year_month | ANALYZE TABLE `covid_github`.`worldwide_aggregated_archive` PARTITION (year_month) COMPUTE STATISTICS;
| default | test |   | ANALYZE TABLE `default`.`test` COMPUTE STATISTICS;|
| priv_dstreev | catalog_sales | cs_sold_date_sk | ANALYZE TABLE `priv_dstreev`.`catalog_sales` PARTITION (cs_sold_date_sk) COMPUTE STATISTICS;
| priv_dstreev | cc_trans |   | ANALYZE TABLE `priv_dstreev`.`cc_trans` COMPUTE STATISTICS;|
| priv_dstreev | my_managed_table |   | ANALYZE TABLE `priv_dstreev`.`my_managed_table` COMPUTE STATISTICS;|
| priv_dstreev | my_multi_part | update_dt,batch | ANALYZE TABLE `priv_dstreev`.`my_multi_part` PARTITION (update_dt,batch) COMPUTE STATISTICS;
| priv_dstreev | my_parquet_001 |   | ANALYZE TABLE `priv_dstreev`.`my_parquet_001` COMPUTE STATISTICS;|
| priv_dstreev | my_test_01 |   | ANALYZE TABLE `priv_dstreev`.`my_test_01` COMPUTE STATISTICS;|
| priv_dstreev | my_test_ncr |   | ANALYZE TABLE `priv_dstreev`.`my_test_ncr` COMPUTE STATISTICS;|
| priv_dstreev | my_test_ncr1 |   | ANALYZE TABLE `priv_dstreev`.`my_test_ncr1` COMPUTE STATISTICS;|
| priv_dstreev | test |   | ANALYZE TABLE `priv_dstreev`.`test` COMPUTE STATISTICS;|
| priv_jonsnow | my_test_01 |   | ANALYZE TABLE `priv_jonsnow`.`my_test_01` COMPUTE STATISTICS;|
| priv_dstreev | test_01_bucket |   | ANALYZE TABLE `priv_dstreev`.`test_01_bucket` COMPUTE STATISTICS;|
| priv_jonsnow | my_test_02 |   | ANALYZE TABLE `priv_jonsnow`.`my_test_02` COMPUTE STATISTICS;|
| priv_dstreev | test_02_bucket |   | ANALYZE TABLE `priv_dstreev`.`test_02_bucket` COMPUTE STATISTICS;|
| priv_dstreev | test_11 |   | ANALYZE TABLE `priv_dstreev`.`test_11` COMPUTE STATISTICS;|
| priv_dstreev | test_array |   | ANALYZE TABLE `priv_dstreev`.`test_array` COMPUTE STATISTICS;|
| priv_dstreev | test_atc |   | ANALYZE TABLE `priv_dstreev`.`test_atc` COMPUTE STATISTICS;|
| priv_winterfell | test_managed |   | ANALYZE TABLE `priv_winterfell`.`test_managed` COMPUTE STATISTICS;|
| credit_card | cc_balance |   | ANALYZE TABLE `credit_card`.`cc_balance` COMPUTE STATISTICS;|
| credit_card | cc_trans | trans_dt | ANALYZE TABLE `credit_card`.`cc_trans` PARTITION (trans_dt) COMPUTE STATISTICS;
| streaming_cc | cc_acct |   | ANALYZE TABLE `streaming_cc`.`cc_acct` COMPUTE STATISTICS;|
| stuff2 | my_managed_test |   | ANALYZE TABLE `stuff2`.`my_managed_test` COMPUTE STATISTICS;|
| credit_card | cc_trans_stream | ingest_cycle | ANALYZE TABLE `credit_card`.`cc_trans_stream` PARTITION (ingest_cycle) COMPUTE STATISTICS;
| stuff2 | test_01 |   | ANALYZE TABLE `stuff2`.`test_01` COMPUTE STATISTICS;|
| streaming_cc | cc_acct_daily | processing_dt | ANALYZE TABLE `streaming_cc`.`cc_acct_daily` PARTITION (processing_dt) COMPUTE STATISTICS;
| streaming_cc | cc_acct_delta | processing_cycle | ANALYZE TABLE `streaming_cc`.`cc_acct_delta` PARTITION (processing_cycle) COMPUTE STATISTICS;
| streaming_cc | cc_mrch_daily | processing_dt | ANALYZE TABLE `streaming_cc`.`cc_mrch_daily` PARTITION (processing_dt) COMPUTE STATISTICS;
| streaming_cc | cc_trans_alt_from_streaming | processing_cycle | ANALYZE TABLE `streaming_cc`.`cc_trans_alt_from_streaming` PARTITION (processing_cycle) COMPUTE STATISTICS;
| streaming_cc | cc_trans_alt_from_streaming_2 | processing_cycle | ANALYZE TABLE `streaming_cc`.`cc_trans_alt_from_streaming_2` PARTITION (processing_cycle) COMPUTE STATISTICS;
| streaming_cc | cc_trans_from_incremental_append | processing_cycle | ANALYZE TABLE `streaming_cc`.`cc_trans_from_incremental_append` PARTITION (processing_cycle) COMPUTE STATISTICS;
| streaming_cc | cc_trans_from_streaming | processing_cycle | ANALYZE TABLE `streaming_cc`.`cc_trans_from_streaming` PARTITION (processing_cycle) COMPUTE STATISTICS;
| streaming_cc | state |   | ANALYZE TABLE `streaming_cc`.`state` COMPUTE STATISTICS;|
| streaming_cc | states |   | ANALYZE TABLE `streaming_cc`.`states` COMPUTE STATISTICS;|
| testing | multi_part | st,update_dt | ANALYZE TABLE `testing`.`multi_part` PARTITION (st,update_dt) COMPUTE STATISTICS;
| test_alt_managed | test_01 |   | ANALYZE TABLE `test_alt_managed`.`test_01` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | call_center |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`call_center` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | catalog_page |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`catalog_page` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | catalog_returns | cr_returned_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`catalog_returns` PARTITION (cr_returned_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_25 | catalog_sales | cs_sold_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`catalog_sales` PARTITION (cs_sold_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_25 | customer |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`customer` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | call_center |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`call_center` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | customer_address |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`customer_address` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | catalog_page |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`catalog_page` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | customer_demographics |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`customer_demographics` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | date_dim |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`date_dim` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | household_demographics |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`household_demographics` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | catalog_returns | cr_returned_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`catalog_returns` PARTITION (cr_returned_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_25 | income_band |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`income_band` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | inventory |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`inventory` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | catalog_sales | cs_sold_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`catalog_sales` PARTITION (cs_sold_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_25 | item |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`item` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | customer |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`customer` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | promotion |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`promotion` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | customer_address |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`customer_address` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | reason |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`reason` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | customer_demographics |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`customer_demographics` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | ship_mode |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`ship_mode` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | date_dim |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`date_dim` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | store |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`store` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | household_demographics |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`household_demographics` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | income_band |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`income_band` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | store_returns | sr_returned_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`store_returns` PARTITION (sr_returned_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | inventory | inv_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`inventory` PARTITION (inv_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | item |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`item` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | store_sales | ss_sold_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`store_sales` PARTITION (ss_sold_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | promotion |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`promotion` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | time_dim |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`time_dim` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | reason |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`reason` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | warehouse |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`warehouse` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | ship_mode |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`ship_mode` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | web_page |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`web_page` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | store |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`store` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | web_returns | wr_returned_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`web_returns` PARTITION (wr_returned_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | store_sales | ss_sold_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`store_sales` PARTITION (ss_sold_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | time_dim |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`time_dim` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | web_sales | ws_sold_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`web_sales` PARTITION (ws_sold_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | warehouse |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`warehouse` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_25 | web_site |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_25`.`web_site` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | web_page |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`web_page` COMPUTE STATISTICS;|
| tpcds_test | catalog_sales | cs_sold_date_sk | ANALYZE TABLE `tpcds_test`.`catalog_sales` PARTITION (cs_sold_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | call_center |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`call_center` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | catalog_page |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`catalog_page` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_250 | web_returns | wr_returned_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`web_returns` PARTITION (wr_returned_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | web_sales | ws_sold_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`web_sales` PARTITION (ws_sold_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | catalog_returns | cr_returned_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`catalog_returns` PARTITION (cr_returned_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_250 | web_site |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_250`.`web_site` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | catalog_sales | cs_sold_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`catalog_sales` PARTITION (cs_sold_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | customer |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`customer` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | customer_address |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`customer_address` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | customer_demographics |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`customer_demographics` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | date_dim |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`date_dim` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | household_demographics |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`household_demographics` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | income_band |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`income_band` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | inventory | inv_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`inventory` PARTITION (inv_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | item |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`item` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | promotion |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`promotion` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | reason |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`reason` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | ship_mode |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`ship_mode` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | store |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`store` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | store_returns | sr_returned_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`store_returns` PARTITION (sr_returned_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | store_sales | ss_sold_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`store_sales` PARTITION (ss_sold_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | time_dim |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`time_dim` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | warehouse |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`warehouse` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | web_page |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`web_page` COMPUTE STATISTICS;|
| tpcds_bin_partitioned_orc_300 | web_returns | wr_returned_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`web_returns` PARTITION (wr_returned_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | web_sales | ws_sold_date_sk | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`web_sales` PARTITION (ws_sold_date_sk) COMPUTE STATISTICS;
| tpcds_bin_partitioned_orc_300 | web_site |   | ANALYZE TABLE `tpcds_bin_partitioned_orc_300`.`web_site` COMPUTE STATISTICS;|
