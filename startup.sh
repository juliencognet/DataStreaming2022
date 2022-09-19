docker-compose up -d
docker exec -ti flink-jobmanager sh -c "sql-client.sh -f /usr/local/flink/createFlinkSqlJob.sql"