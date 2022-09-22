/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cgi.datastream.conference;

import com.cgi.datastream.conference.kafka_serialization.MeterValueWithReferenceDataKeySerializer;
import com.cgi.datastream.conference.kafka_serialization.MeterValueWithReferenceDataValueSerializer;
import com.cgi.datastream.conference.operations.DeserializeMeter;
import com.cgi.datastream.conference.operations.DeserializeMeterValue;
import com.cgi.datastream.conference.operations.JoinMeterReferenceAndMeterValues;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Flink DataStream Job.
 *
 * <p>For a tutorial how to write a Flink application, check the
 * tutorials and examples on the <a href="https://flink.apache.org">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 */
public class DataStreamJob {

	static String brokers = "kafka:9092";
	static ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) throws Exception {
		// Sets up the execution environment, which is the main entry point
		// to building Flink applications.
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// Define Kafka sources and sink
		KafkaSource<String> source = buildInputKafkaSource(brokers, "input-meter-values");
		KafkaSource<String> changeDataCaptureSource = buildInputKafkaSource(brokers, "input-data-reference-changes");
		KafkaSink<MeterValueWithReferenceData> sink = buildOutputKafkaSink(brokers, objectMapper);

		// Define Reference Data Change Data Capture Stream
		SingleOutputStreamOperator<Meter[]> streamChangeDataCaptureSource =
				env.fromSource(changeDataCaptureSource, WatermarkStrategy.noWatermarks(), "CDC Kafka Source")
						.map(new DeserializeMeter(objectMapper));

		// Define Meter Values Stream (connect and join with reference data from CDC)
		env.fromSource(source, WatermarkStrategy.noWatermarks(), "MeterValues Kafka Source")
				.map(new DeserializeMeterValue(objectMapper))
				.connect(streamChangeDataCaptureSource)
				.flatMap(new JoinMeterReferenceAndMeterValues())
				.filter(meterValueWithReferenceData -> meterValueWithReferenceData.building!=null)
				.keyBy(MeterValueWithReferenceData::getBuilding)
				.sinkTo(sink);

		// 10 automatic restarts (with delay of 1 seconds between each restart)
		env.setRestartStrategy(RestartStrategies.fixedDelayRestart(10, 1000));
		
		// Execute program, beginning computation.
		env.execute("Flink job to enrich meter value with reference data and output to Kafka");
	}

	private static KafkaSink<MeterValueWithReferenceData> buildOutputKafkaSink(String brokers, ObjectMapper objectMapper) {
		KafkaSink<MeterValueWithReferenceData> sink = KafkaSink.<MeterValueWithReferenceData>builder()
				.setBootstrapServers(brokers)
				.setRecordSerializer(KafkaRecordSerializationSchema.<MeterValueWithReferenceData>builder()
					.setTopic("output-meter-values-with-reference-data-from-stream-api")
					.setValueSerializationSchema(new MeterValueWithReferenceDataValueSerializer(objectMapper))
					.setKeySerializationSchema(new MeterValueWithReferenceDataKeySerializer(objectMapper))
					.build()
				)
				.setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
				.build();
		return sink;
	}

	private static KafkaSource<String> buildInputKafkaSource(String brokers, String x) {
		KafkaSource<String> source = KafkaSource.<String>builder()
				.setBootstrapServers(brokers)
				.setTopics(x)
				.setGroupId("consumer-group-flink-stream-api")
				.setStartingOffsets(OffsetsInitializer.earliest())
				.setValueOnlyDeserializer(new SimpleStringSchema())
				.build();
		return source;
	}
}
