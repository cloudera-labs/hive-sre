# Managed Compactions

```sql
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
```