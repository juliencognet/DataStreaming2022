echo ">> Starting the full stack of the demo"
docker-compose up -d

echo ">> Building Flink Stream Java Api"
cd flink/dataStreamJavaApiDemo/
mvn clean install
cd ../..

echo ">> Deploy Flink Stream Java Api Demo"
docker exec flink-jobmanager sh -c "flink list -r | grep \"Flink job to \" | cut -f 4 -d \" \" | while read -r line ; do flink stop --savepointPath /tmp/savepoint $line; done"
docker exec flink-jobmanager sh -c "flink run --detached /home/jars/flinkdemo-1.0-SNAPSHOT.jar"

echo ">> Deploy Flink SQL Api Demo"
docker exec -ti flink-jobmanager sh -c "sql-client.sh -f /usr/local/flink/createFlinkSqlJob.sql"