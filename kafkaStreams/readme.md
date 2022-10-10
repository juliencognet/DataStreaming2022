You can test on local with the following steps:

start a local stack using docker :
cd devtools \ docker-compose up -d --force-recreate

Generate a jar :
cd .. \ gradlew.bat jar

Launch it :
java -jar .\build\libs\stream_processor_dispatcher-0.0.1-SNAPSHOT.jar .\configuration\local.properties

You can also check the topology directly into the logs searching ---- TOPOLOGY ----. \ Copy/paste it to the following website to visualize it: https://zz85.github.io/kafka-streams-viz/