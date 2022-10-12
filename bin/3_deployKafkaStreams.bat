cd /D "%~dp0"
cd ../kafkaStreams
call mvn clean install
cd ..
start docker run --name kafkaStreams --network talk-streaming-network -v %cd%/kafkaStreams/target:/tmp/kafkaStreams adoptopenjdk/openjdk11 java -jar /tmp/kafkaStreams/kafka-stream-demo-1.0-SNAPSHOT.jar
