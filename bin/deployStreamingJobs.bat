echo off
echo -----------------------------------------
echo -- Data Streaming Demo                 --
echo -----------------------------------------
echo -                                     
cd /D "%~dp0"
echo -- Building Flink Stream Java Api --
cd ../flink/stream_java_api/
call mvn clean install
cd ../..

echo -- Stopping running streams --
docker exec flink-jobmanager sh -c "flink list -r | grep RUNNING | cut -f 4 -d \" \" | while read -r line ; do flink stop --savepointPath /tmp/savepoint $line; done"

echo -- Deploy Flink Stream Java Api Demo --
docker exec flink-jobmanager sh -c "flink run --detached /usr/local/flink/stream_java_api/flink-stream-api-demo-1.0-SNAPSHOT.jar"

echo -- Deploy Flink SQL Api Demo --
docker exec -ti flink-jobmanager sh -c "sql-client.sh -f /usr/local/flink/table_api/createFlinkSqlJob.sql"
