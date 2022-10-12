echo off
echo -----------------------------------------
echo -- Data Streaming Demo                 --
echo -----------------------------------------
echo -
echo -- Reinit the stack --
cd /D "%~dp0"
cd ..
echo -- Stopping running streams --
docker exec flink-jobmanager sh -c "flink list -r | grep RUNNING | cut -f 4 -d \" \" | while read -r line ; do flink stop --savepointPath /tmp/savepoint $line; done"

echo -- Stop and remove kafka and influxdb
docker-compose stop kafka influxdb ksqldb-server ksqldb-cli telegraf
docker-compose rm kafka influxdb ksqldb-server ksqldb-cli telegraf

