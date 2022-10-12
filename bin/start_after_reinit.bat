echo off
echo -----------------------------------------
echo -- Data Streaming Demo                 --
echo -----------------------------------------
echo -
echo -- Starting the full stack of the demo --
cd /D "%~dp0"
cd ..
docker-compose up -d

pause 20
docker-compose up -d telegraf

echo -- Deploying KSQLDb
docker exec -it ksqldb-cli sh -c "cat /tmp/ksqldb/command_ksql.sql | ksql http://ksqldb-server:8088"