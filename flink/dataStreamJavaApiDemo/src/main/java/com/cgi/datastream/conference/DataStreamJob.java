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

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.util.Collector;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Skeleton for a Flink DataStream Job.
 *
 * <p>For a tutorial how to write a Flink application, check the
 * tutorials and examples on the <a href="https://flink.apache.org">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class DataStreamJob {

	public static void main(String[] args) throws Exception {
		// Sets up the execution environment, which is the main entry point
		// to building Flink applications.
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		String brokers = "kafka:9092";
		ObjectMapper objectMapper = new ObjectMapper();

		KafkaSource<String> source = KafkaSource.<String>builder()
				.setBootstrapServers(brokers)
				.setTopics("test-topic")
				.setGroupId("flink-group")
				.setStartingOffsets(OffsetsInitializer.earliest())
				.setValueOnlyDeserializer(new SimpleStringSchema())
				.build();

		KafkaSource<String> changeDataCaptureSource = KafkaSource.<String>builder()
				.setBootstrapServers(brokers)
				.setTopics("data-reference-changes")
				.setGroupId("flink-group")
				.setStartingOffsets(OffsetsInitializer.earliest())
				.setValueOnlyDeserializer(new SimpleStringSchema())
				.build();

		KafkaSink<String> sink = KafkaSink.<String>builder()
				.setBootstrapServers(brokers)
				.setRecordSerializer(KafkaRecordSerializationSchema.builder()
						.setTopic("output-topic")
						.setValueSerializationSchema(new SimpleStringSchema())
						.build()
				)
				.setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
				.build();

		SingleOutputStreamOperator<Meter[]> streamChangeDataCaptureSource =
				env.fromSource(changeDataCaptureSource, WatermarkStrategy.noWatermarks(), "CDC Kafka Source")
						.map((MapFunction<String, Meter[]>) s -> objectMapper.readValue(s,Meter[].class));

		DataStreamSource<String> streamSource =
				env.fromSource(source, WatermarkStrategy.noWatermarks(), "MeterValues Kafka Source");

		streamSource
				.map((MapFunction<String, MeterValue>) s -> objectMapper.readValue(s, MeterValue.class))
				.connect(streamChangeDataCaptureSource)
				.flatMap(new CoFlatMapFunction<MeterValue, Meter[], MeterValueWithReferenceData>() {

					final HashMap<String,Meter> meters = new HashMap<>();

					@Override
					public void flatMap1(MeterValue meterValue, Collector<MeterValueWithReferenceData> collector) {
						Meter currentMeter = meters.get(meterValue.meterId);
						MeterValueWithReferenceData meterValueWithReferenceData = new MeterValueWithReferenceData();
						meterValueWithReferenceData.meterId = currentMeter.idMeter;
						meterValueWithReferenceData.meterName = currentMeter.meterName;
						meterValueWithReferenceData.meterTimestamp = meterValue.meterTimestamp;
						meterValueWithReferenceData.meterValue = meterValue.meterValue;
						collector.collect(meterValueWithReferenceData);
					}

					@Override
					public void flatMap2(Meter[] meter, Collector<MeterValueWithReferenceData> collector) {
						for (Meter meterElement : meter) {
							meters.put(meterElement.idMeter,meterElement);
						}
					}
				})
				.map((MapFunction<MeterValueWithReferenceData, String>) objectMapper::writeValueAsString)
				.sinkTo(sink);

		// Execute program, beginning computation.
		env.setRestartStrategy(RestartStrategies.fixedDelayRestart(10, 1000));
		env.execute("Flink job to enrich meter value from data reference and output to Kafka");

	}
}
