docker exec flink-jobmanager sh -c "flink list -r | grep \"Flink job to \" | cut -f 4 -d \" \" | while read -r line ; do flink stop --savepointPath /tmp/savepoint $line; done"
docker exec flink-jobmanager flink run --detached /home/jars/flinkdemo-1.0-SNAPSHOT.jar