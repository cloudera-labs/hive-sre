## Hive JDBC Performance Testing Tool (perf)

JDBC Performance Testing tool.  Will provide connection timing details and rolling windows of performance for long running queries.  Details in the windows will show not only records but also an estimate of the data volume.

*Example Output*
```
========== v.2.0.1-SNAPSHOT ===========
URL        : jdbc:hive2://os04.streever.local:2181,os05.streever.local:2181,os10.streever.local:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2;principal=hive/_HOST@STREEVER.LOCAL
Batch Size : 10000
SQL        : SELECT field1_1,field1_2,field1_3,field1_4 FROM perf_test.wide_table
Lite       : false
----------------------------
Connect Attempt  : 0ms
Connected        : 2201ms
Create Statement : 2205ms
Before Query     : 2205ms
Query Return     : 2697ms
Start Iterating Results   : 2698ms
Completed Iterating Results: 79408ms
Statement Closed           : 79452ms
Resultset Closed           : 79452ms
Process Completed          : 79471ms

----------------------------
Window Length(ms) | Record Average | Records per/sec | Data Size per/sec
60000		7710000		128500		14970260
180000		10020000		125250		14642405
300000		10020000		125250		14642405
600000		10020000		125250		14642405

===========================
Running for: 80966ms		Started: 2020-03-06 13:57:40.492		Record Count: 10020000		Data Size: 1171392406
```

**Environment and Connection via Knox**

*Example*  Note: The additional `cp` setting with `hadoop classpath` is required when connecting to a Kerberized endpoint.
```
URL="jdbc:hive2://os06.streever.local:8443/;ssl=true;sslTrustStore=/home/dstreev/certs/bm90-gateway.jks;trustStorePassword=hortonworks;transportMode=http;httpPath=gateway/default/hive"
QUERY="SELECT field1_1,field1_2,field1_3,field1_4 FROM perf_test.wide_table"
BATCH_SIZE=10000
PW=<set_me>

hive-sre perf -u "${URL}" -e "${QUERY}" -b $BATCH_SIZE -n ${USER} -p <password> 
```

**Environment and Connection via Kerberos from Edge**

*Example* Note: Additional hadoop libraries are required for a kerberized connection.  Use `--hadoop-classpath` in the commandline to call the environments hadoop classpath and add it to the `cp` of the application.
```
URL="jdbc:hive2://os05.streever.local:10601/default;httpPath=cliservice;principal=hive/_HOST@STREEVER.LOCAL;transportMode=http"
QUERY="SELECT field1_1,field1_2,field1_3,field1_4 FROM perf_test.wide_table"
# Note that `hadoop classpath` statement to bring in all necessary libs.
BATCH_SIZE=10000

hive-sre --hadoop-classpath perf -u "${URL}" -e "${QUERY}" -b $BATCH_SIZE 
```

**Environment and Connection via Kerberos from a Client Host (Non-Edge)**

Even with a valid Kerberos ticket, this type of host will not have all the `hadoop` libs we get from `hadoop classpath` to work.  I have not yet been able to find the right mix of classes to add to the 'uber' jar to get this working.

