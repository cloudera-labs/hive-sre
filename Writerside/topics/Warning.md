# Warning

## Building METADATA

Rebuilding METADATA can be an expensive scenario.  Especially when you are trying to reconstruct the entire metastore in a short time period, consider this in your planning.  Know the number of partitions and buckets you will be moving and account for this.  Test on smaller datasets (volume and metadata elements).  Progress to testing higher volumes/partition counts to find limits and make adjustments to your strategy.

Using the SQL and EXPORT_IMPORT strategies will move metadata AND data, but rebuilding the metastore elements can be pretty expensive.  So consider migrating the metadata separately from the data (distcp) and use MSCK on the RIGHT cluster to discover the data.  This will be considerably more efficient.

If you will be doing a lot of metadata work on the RIGHT cluster. That cluster also serves a current user base; consider setting up separate HS2 pods for the migration to minimize the impact on the current user community. [Isolate Migration Activities](hms-mirror-optimizations.md#isolate-migration-activities)

## Partition Handling for Data Transfers

There are three settings in the configuration to control how and to what extent we'll attempt to migrate *DATA* for tables with partitions.

For non-ACID/transactional tables the setting in:

```yaml
hybrid:
  exportImportPartitionLimit: 100
  sqlPartitionLimit: 500
```

Control both the `HYBRID` strategy for selecting either `EXPORT_IMPORT` or `SQL` and the `SQL` *LIMIT* for how many partitions we'll attempt.  When the `SQL` limit is exceeded, you will need to use `SCHEMA_ONLY` to migrate the schema followed by `distcp` to move the data.

For ACID/transactional tables, the setting in:

```yaml
migrateACID:
  partitionLimit: 500
```

Effectively draws the same limit as above.

Why do we have these limits?  Mass migration of datasets via SQL and EXPORT_IMPORT with many partitions is costly and NOT very efficient.  It's best that when these limits are reached that you separate the METADATA and DATA migration to DDL and distcp.

## Permissions

We use a cross-cluster technique to back metadata in the RIGHT cluster with datasets in the LEFT cluster for data strategies: LINKED, HYBRID, EXPORT_IMPORT, SQL, and SCHEMA_ONLY (with `-ams` AVRO Migrate Schema).

See [Linking Clusters Storage Layers](Linking-Cluster-Storage-Layers.md) for details on configuring this state.
