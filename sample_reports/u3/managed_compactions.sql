-- Copyright 2021 Cloudera, Inc. All Rights Reserved.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

-- Hive 3 - Compaction Check
ALTER TABLE covid_ga.county_cases_archive PARTITION (year_month="RUN_1") COMPACT "MAJOR";
ALTER TABLE covid_ga.county_cases_archive PARTITION (year_month="2020-08") COMPACT "MAJOR";
ALTER TABLE covid_ga.county_cases_archive PARTITION (year_month="2020-09") COMPACT "MAJOR";
ALTER TABLE covid_ga.deaths_archive PARTITION (year_month="RUN_1") COMPACT "MAJOR";
ALTER TABLE covid_ga.deaths_archive PARTITION (year_month="2020-08") COMPACT "MAJOR";
ALTER TABLE covid_ga.deaths_archive PARTITION (year_month="2020-09") COMPACT "MAJOR";
ALTER TABLE covid_ga.demographics_archive PARTITION (year_month="RUN_1") COMPACT "MAJOR";
ALTER TABLE covid_ga.demographics_archive PARTITION (year_month="2020-08") COMPACT "MAJOR";
ALTER TABLE covid_ga.demographics_archive PARTITION (year_month="2020-09") COMPACT "MAJOR";
ALTER TABLE covid_github.countries_aggregated_archive PARTITION (year_month="RUN_1") COMPACT "MAJOR";
ALTER TABLE covid_github.countries_aggregated_archive PARTITION (year_month="2020-08") COMPACT "MAJOR";
ALTER TABLE covid_github.countries_aggregated_archive PARTITION (year_month="2020-09") COMPACT "MAJOR";
ALTER TABLE covid_github.key_countries_pivoted_archive PARTITION (year_month="RUN_1") COMPACT "MAJOR";
ALTER TABLE covid_github.key_countries_pivoted_archive PARTITION (year_month="2020-08") COMPACT "MAJOR";
ALTER TABLE covid_github.key_countries_pivoted_archive PARTITION (year_month="2020-09") COMPACT "MAJOR";
ALTER TABLE covid_github.reference_archive PARTITION (year_month="RUN_1") COMPACT "MAJOR";
ALTER TABLE covid_github.reference_archive PARTITION (year_month="2020-08") COMPACT "MAJOR";
ALTER TABLE covid_github.reference_archive PARTITION (year_month="2020-09") COMPACT "MAJOR";
ALTER TABLE covid_github.time_series_combined_archive PARTITION (year_month="RUN_1") COMPACT "MAJOR";
ALTER TABLE covid_github.time_series_combined_archive PARTITION (year_month="2020-08") COMPACT "MAJOR";
ALTER TABLE covid_github.time_series_combined_archive PARTITION (year_month="2020-09") COMPACT "MAJOR";
ALTER TABLE covid_github.us_confirmed_archive PARTITION (year_month="RUN_1") COMPACT "MAJOR";
ALTER TABLE covid_github.us_confirmed_archive PARTITION (year_month="2020-08") COMPACT "MAJOR";
ALTER TABLE covid_github.us_confirmed_archive PARTITION (year_month="2020-09") COMPACT "MAJOR";
