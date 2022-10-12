echo -- Stopping running streams --
docker exec flink-jobmanager sh -c "flink list -r | grep RUNNING | cut -f 4 -d \" \" | while read -r line ; do flink stop --savepointPath /tmp/savepoint $line; done"

echo -- Deploy Flink SQL Api Demo --
docker exec -ti flink-jobmanager sh -c "sql-client.sh -f /usr/local/flink/table_api/createFlinkSqlJob.sql"
