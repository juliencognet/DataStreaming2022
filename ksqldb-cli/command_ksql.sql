CREATE STREAM RAW_DATA (meterTimestamp VARCHAR, meterId VARCHAR,meterValue VARCHAR)    WITH (kafka_topic='T1-1-raw-data', partitions=1, value_format='json');
	
CREATE STREAM "T1-1-raw-data-numeric"  WITH (KAFKA_TOPIC='T1-1-raw-data-numeric')AS    SELECT meterId, AS_VALUE(meterId) AS "meterId", meterTimestamp, CAST(meterValue AS DOUBLE) AS meterValue    FROM RAW_DATA  PARTITION BY meterId   EMIT CHANGES;
