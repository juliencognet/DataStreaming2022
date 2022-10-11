echo off
echo -----------------------------------------
echo -- Data Streaming Demo                 --
echo -----------------------------------------
echo -
echo -- Starting the full stack of the demo --
cd /D "%~dp0"
cd ..
docker-compose up -d

echo -- Deploying the streaming jobs --
cd bin/
deployStreamingJobs.bat