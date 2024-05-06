# Help Options

```
usage: hms-mirror <options>
                  version: xxx
Hive Metastore Migration Utility
 -accept,--accept                                              Accept ALL confirmations and silence
                                                               prompts
 -ap,--acid-partition-count <limit>                            Set the limit of partitions that the
                                                               ACID strategy will work with. '-1'
                                                               means no-limit.
 -asm,--avro-schema-migration                                  Migrate AVRO Schema Files referenced
                                                               in TBLPROPERTIES by
                                                               'avro.schema.url'.  Without migration
                                                               it is expected that the file will
                                                               exist on the other cluster and match
                                                               the 'url' defined in the schema DDL.
                                                               If it's not present, schema creation
                                                               will FAIL.
                                                               Specifying this option REQUIRES the
                                                               LEFT and RIGHT cluster to be LINKED.
                                                               See docs:
                                                               https://github.com/cloudera-labs/hms-
                                                               mirror#linking-clusters-storage-layer
                                                               s
 -at,--auto-tune                                               Auto-tune Session Settings for
                                                               SELECT's and DISTRIBUTION for
                                                               Partition INSERT's.
 -cfg,--config <filename>                                      Config with details for the
                                                               HMS-Mirror.  Default:
                                                               $HOME/.hms-mirror/cfg/default.yaml
 -cine,--create-if-not-exist                                   CREATE table/partition statements
                                                               will be adjusted to include 'IF NOT
                                                               EXISTS'.  This will ensure all
                                                               remaining sql statements will be run.
                                                               This can be used to sync partition
                                                               definitions for existing tables.
 -cs,--common-storage <storage-path>                           Common Storage used with Data
                                                               Strategy HYBRID, SQL, EXPORT_IMPORT.
                                                               This will change the way these
                                                               methods are implemented by using the
                                                               specified storage location as an
                                                               'common' storage point between two
                                                               clusters.  In this case, the cluster
                                                               do NOT need to be 'linked'.  Each
                                                               cluster DOES need to have access to
                                                               the location and authorization to
                                                               interact with the location.  This may
                                                               mean additional configuration
                                                               requirements for 'hdfs' to ensure
                                                               this seamless access.
 -cto,--compress-test-output                                   Data movement (SQL/STORAGE_MIGRATION)
                                                               of TEXT based file formats will be
                                                               compressed in the new table.
 -d,--data-strategy <strategy>                                 Specify how the data will follow the
                                                               schema. [DUMP, SCHEMA_ONLY, LINKED,
                                                               SQL, EXPORT_IMPORT, HYBRID,
                                                               CONVERT_LINKED, STORAGE_MIGRATION,
                                                               COMMON, ICEBERG_CONVERSION]
 -da,--downgrade-acid                                          Downgrade ACID tables to EXTERNAL
                                                               tables with purge.
 -db,--database <databases>                                    Comma separated list of Databases
                                                               (upto 100).
 -dbo,--database-only                                          Migrate the Database definitions as
                                                               they exist from LEFT to RIGHT
 -dbp,--db-prefix <prefix>                                     Optional: A prefix to add to the
                                                               RIGHT cluster DB Name. Usually used
                                                               for testing.
 -dbr,--db-rename <rename>                                     Optional: Rename target db to ...
                                                               This option is only valid when '1'
                                                               database is listed in `-db`.
 -dbRegEx,--database-regex <regex>                             RegEx of Database to include in
                                                               process.
 -dc,--distcp <flow-direction default:PULL>                    Build the 'distcp' workplans.
                                                               Optional argument (PULL, PUSH) to
                                                               define which cluster is running the
                                                               distcp commands.  Default is PULL.
 -dp,--decrypt-password <encrypted-password>                   Used this in conjunction with '-pkey'
                                                               to decrypt the generated passcode
                                                               from `-p`.
 -ds,--dump-source <source>                                    Specify which 'cluster' is the source
                                                               for the DUMP strategy (LEFT|RIGHT).
 -dtd,--dump-test-data                                         Used to dump a data set that can be
                                                               feed into the process for testing.
 -e,--execute                                                  Execute actions request, without this
                                                               flag the process is a dry-run.
 -ep,--export-partition-count <limit>                          Set the limit of partitions that the
                                                               EXPORT_IMPORT strategy will work
                                                               with.
 -epl,--evaluate-partition-location                            For SCHEMA_ONLY and DUMP
                                                               data-strategies, review the partition
                                                               locations and build partition
                                                               metadata calls to create them is they
                                                               can't be located via 'MSCK'.
 -ewd,--external-warehouse-directory <path>                    The external warehouse directory
                                                               path.  Should not include the
                                                               namespace OR the database directory.
                                                               This will be used to set the LOCATION
                                                               database option.
 -f,--flip                                                     Flip the definitions for LEFT and
                                                               RIGHT.  Allows the same config to be
                                                               used in reverse.
 -fel,--force-external-location                                Under some conditions, the LOCATION
                                                               element for EXTERNAL tables is
                                                               removed (ie: -rdl).  In which case we
                                                               rely on the settings of the database
                                                               definition to control the EXTERNAL
                                                               table data location.  But for some
                                                               older Hive versions, the LOCATION
                                                               element in the database is NOT
                                                               honored.  Even when the database
                                                               LOCATION is set, the EXTERNAL table
                                                               LOCATION defaults to the system wide
                                                               warehouse settings.  This flag will
                                                               ensure the LOCATION element remains
                                                               in the CREATE definition of the table
                                                               to force it's location.
 -glm,--global-location-map <key=value>                        Comma separated key=value pairs of
                                                               Locations to Map. IE:
                                                               /myorig/data/finance=/data/ec/finance
                                                               . This reviews 'EXTERNAL' table
                                                               locations for the path
                                                               '/myorig/data/finance' and replaces
                                                               it with '/data/ec/finance'.  Option
                                                               can be used alone or with -rdl. Only
                                                               applies to 'EXTERNAL' tables and if
                                                               the tables location doesn't contain
                                                               one of the supplied maps, it will be
                                                               translated according to -rdl rules if
                                                               -rdl is specified.  If -rdl is not
                                                               specified, the conversion for that
                                                               table is skipped.
 -h,--help                                                     Help
 -ip,--in-place                                                Downgrade ACID tables to EXTERNAL
                                                               tables with purge.
 -is,--intermediate-storage <storage-path>                     Intermediate Storage used with Data
                                                               Strategy HYBRID, SQL, EXPORT_IMPORT.
                                                               This will change the way these
                                                               methods are implemented by using the
                                                               specified storage location as an
                                                               intermediate transfer point between
                                                               two clusters.  In this case, the
                                                               cluster do NOT need to be 'linked'.
                                                               Each cluster DOES need to have access
                                                               to the location and authorization to
                                                               interact with the location.  This may
                                                               mean additional configuration
                                                               requirements for 'hdfs' to ensure
                                                               this seamless access.
 -itpo,--iceberg-table-property-overrides <key=value>          Comma separated key=value pairs of
                                                               Iceberg Table Properties to
                                                               set/override.
 -iv,--iceberg-version <version>                               Specify the Iceberg Version to use.
                                                               Specify 1 or 2.  Default is 2.
 -ltd,--load-test-data <file>                                  Use the data saved by the `-dtd`
                                                               option to test the process.
 -ma,--migrate-acid <bucket-threshold (2)>                     Migrate ACID tables (if strategy
                                                               allows). Optional:
                                                               ArtificialBucketThreshold count that
                                                               will remove the bucket definition if
                                                               it's below this.  Use this as a way
                                                               to remove artificial bucket
                                                               definitions that were added
                                                               'artificially' in legacy Hive.
                                                               (default: 2)
 -mao,--migrate-acid-only <bucket-threshold (2)>               Migrate ACID tables ONLY (if strategy
                                                               allows). Optional:
                                                               ArtificialBucketThreshold count that
                                                               will remove the bucket definition if
                                                               it's below this.  Use this as a way
                                                               to remove artificial bucket
                                                               definitions that were added
                                                               'artificially' in legacy Hive.
                                                               (default: 2)
 -mnn,--migrate-non-native <arg>                               Migrate Non-Native tables (if
                                                               strategy allows). These include table
                                                               definitions that rely on external
                                                               connection to systems like: HBase,
                                                               Kafka, JDBC
 -mnno,--migrate-non-native-only                               Migrate Non-Native tables (if
                                                               strategy allows). These include table
                                                               definitions that rely on external
                                                               connection to systems like: HBase,
                                                               Kafka, JDBC
 -np,--no-purge                                                For SCHEMA_ONLY, COMMON, and LINKED
                                                               data strategies set RIGHT table to
                                                               NOT purge on DROP
 -o,--output-dir <outputdir>                                   Output Directory (default:
                                                               $HOME/.hms-mirror/reports/<yyyy-MM-dd
                                                               _HH-mm-ss>
 -p,--password <password>                                      Used this in conjunction with '-pkey'
                                                               to generate the encrypted password
                                                               that you'll add to the configs for
                                                               the JDBC connections.
 -pkey,--password-key <password-key>                           The key used to encrypt / decrypt the
                                                               cluster jdbc passwords.  If not
                                                               present, the passwords will be
                                                               processed as is (clear text) from the
                                                               config file.
 -po,--property-overrides <key=value>                          Comma separated key=value pairs of
                                                               Hive properties you wish to
                                                               set/override.
 -pol,--property-overrides-left <key=value>                    Comma separated key=value pairs of
                                                               Hive properties you wish to
                                                               set/override for LEFT cluster.
 -por,--property-overrides-right <key=value>                   Comma separated key=value pairs of
                                                               Hive properties you wish to
                                                               set/override for RIGHT cluster.
 -q,--quiet                                                    Reduce screen reporting output.  Good
                                                               for background processes with output
                                                               redirects to a file
 -rdl,--reset-to-default-location                              Strip 'LOCATION' from all target
                                                               cluster definitions.  This will allow
                                                               the system defaults to take over and
                                                               define the location of the new
                                                               datasets.
 -replay,--replay <report-directory>                           Use to replay process from the report
                                                               output.
 -rid,--right-is-disconnected                                  Don't attempt to connect to the
                                                               'right' cluster and run in this mode
 -ro,--read-only                                               For SCHEMA_ONLY, COMMON, and LINKED
                                                               data strategies set RIGHT table to
                                                               NOT purge on DROP. Intended for use
                                                               with replication distcp strategies
                                                               and has restrictions about existing
                                                               DB's on RIGHT and PATH elements.  To
                                                               simply NOT set the purge flag for
                                                               applicable tables, use -np.
 -rr,--reset-right                                             Use this for testing to remove the
                                                               database on the RIGHT using CASCADE.
 -s,--sync                                                     For SCHEMA_ONLY, COMMON, and LINKED
                                                               data strategies.  Drop and Recreate
                                                               Schema's when different.  Best to use
                                                               with RO to ensure table/partition
                                                               drops don't delete data. When used
                                                               WITHOUT `-tf` it will compare all the
                                                               tables in a database and sync
                                                               (bi-directional).  Meaning it will
                                                               DROP tables on the RIGHT that aren't
                                                               in the LEFT and ADD tables to the
                                                               RIGHT that are missing.  When used
                                                               with `-ro`, table schemas can be
                                                               updated by dropping and recreating.
                                                               When used with `-tf`, only the tables
                                                               that match the filter (on both sides)
                                                               will be considered.
 -sdpi,--sort-dynamic-partition-inserts                        Used to set
                                                               `hive.optimize.sort.dynamic.partition
                                                               ` in TEZ for optimal partition
                                                               inserts.  When not specified, will
                                                               use prescriptive sorting by adding
                                                               'DISTRIBUTE BY' to transfer SQL.
                                                               default: false
 -sf,--skip-features                                           Skip Features evaluation.
 -slc,--skip-link-check                                        Skip Link Check. Use when going
                                                               between or to Cloud Storage to avoid
                                                               having to configure hms-mirror with
                                                               storage credentials and libraries.
                                                               This does NOT preclude your Hive
                                                               Server 2 and compute environment from
                                                               such requirements.
 -slt,--skip-legacy-translation                                Skip Schema Upgrades and Serde
                                                               Translations
 -smn,--storage-migration-namespace <namespace>                Optional: Used with the 'data
                                                               strategy STORAGE_MIGRATION to specify
                                                               the target namespace.
 -so,--skip-optimizations                                      Skip any optimizations during data
                                                               movement, like dynamic sorting or
                                                               distribute by
 -sp,--sql-partition-count <limit>                             Set the limit of partitions that the
                                                               SQL strategy will work with. '-1'
                                                               means no-limit.
 -sql,--sql-output                                             <deprecated>.  This option is no
                                                               longer required to get SQL out in a
                                                               report.  That is the default
                                                               behavior.
 -ssc,--skip-stats-collection                                  Skip collecting basic FS stats for a
                                                               table.  This WILL affect the
                                                               optimizer and our ability to
                                                               determine the best strategy for
                                                               moving data.
 -su,--setup                                                   Setup a default configuration file
                                                               through a series of questions
 -tef,--table-exclude-filter <regex>                           Filter tables (excludes) with name
                                                               matching RegEx. Comparison done with
                                                               'show tables' results.  Check case,
                                                               that's important.  Hive tables are
                                                               generally stored in LOWERCASE. Make
                                                               sure you double-quote the expression
                                                               on the commandline.
 -tf,--table-filter <regex>                                    Filter tables (inclusive) with name
                                                               matching RegEx. Comparison done with
                                                               'show tables' results.  Check case,
                                                               that's important.  Hive tables are
                                                               generally stored in LOWERCASE. Make
                                                               sure you double-quote the expression
                                                               on the commandline.
 -tfp,--table-filter-partition-count-limit <partition-count>   Filter partition tables OUT that are
                                                               have more than specified here. Non
                                                               Partitioned table aren't filtered.
 -tfs,--table-filter-size-limit <size MB>                      Filter tables OUT that are above the
                                                               indicated size.  Expressed in MB
 -to,--transfer-ownership                                      If available (supported) on LEFT
                                                               cluster, extract and transfer the
                                                               tables owner to the RIGHT cluster.
                                                               Note: This will make an 'exta' SQL
                                                               call on the LEFT cluster to determine
                                                               the ownership.  This won't be
                                                               supported on CDH 5 and some other
                                                               legacy Hive platforms. Beware the
                                                               cost of this extra call for EVERY
                                                               table, as it may slow down the
                                                               process for a large volume of tables.
 -v,--views-only                                               Process VIEWs ONLY
 -wd,--warehouse-directory <path>                              The warehouse directory path.  Should
                                                               not include the namespace OR the
                                                               database directory. This will be used
                                                               to set the MANAGEDLOCATION database
                                                               option.

```
