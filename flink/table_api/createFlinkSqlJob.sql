SET sql-client.execution.result-mode=TABLEAU;

CREATE TABLE KafkaTable (
  `meterId` STRING,
  `meterValue` STRING,
  corrMeterValue as cast(`meterValue` as Double),
  `meterTimestamp` STRING,
  ts as to_timestamp(from_unixtime(cast(floor(cast(`meterTimestamp` as Double)/1000) as Integer))),
  WATERMARK FOR ts AS ts - INTERVAL '5' SECOND,
  proctime as PROCTIME()
) WITH (
  'connector' = 'kafka',
  'topic' = 'test-topic',
  'properties.bootstrap.servers' = 'kafka:9092',
  'properties.group.id' = 'testGroup',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'json',
  'json.fail-on-missing-field' = 'false',
  'json.ignore-parse-errors' = 'true'
);

CREATE TABLE MeterReference (
  ID_METER STRING,
  METER_NAME STRING
) WITH (
   'connector' = 'jdbc',
   'url' = 'jdbc:postgresql://datareference:5432/referencedata',
   'username' = 'postgres',
   'password' = 'password', 
   'table-name' = 'METER'
);

CREATE TABLE MeterWithDataReference (
  `meterId` STRING PRIMARY KEY,
  corrMeterValue DOUBLE,
  `meterName` STRING,
  ts TIMESTAMP
) WITH (
  'connector' = 'upsert-kafka',
  'topic' = 'meter-with-reference-data-topic',
  'properties.bootstrap.servers' = 'kafka:9092',
  'properties.group.id' = 'testGroup',
  'key.format' = 'json',
  'value.format' = 'json'
);

INSERT INTO MeterWithDataReference
SELECT k.meterId, k.corrMeterValue, r.METER_NAME, k.ts
FROM KafkaTable k 
LEFT JOIN MeterReference r ON k.meterId = r.ID_METER;