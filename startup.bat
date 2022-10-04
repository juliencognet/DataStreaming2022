echo off
echo -----------------------------------------
echo -- Data Streaming Demo                 --
echo -----------------------------------------
echo -
echo -- Starting the full stack of the demo --
docker-compose up -d

echo -- Deploying the streaming jobs --
deployStreamingJobs.bat