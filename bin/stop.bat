echo off
echo -----------------------------------------
echo -- Data Streaming Demo                 --
echo -----------------------------------------
echo -
echo -- Stopping the full stack of the demo --
cd /D "%~dp0"
cd ..
docker-compose stop