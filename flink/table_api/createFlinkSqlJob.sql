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
  'topic' = 'input-meter-values',
  'properties.bootstrap.servers' = 'kafka:9092',
  'properties.group.id' = 'consumer-group-table-api',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'json',
  'json.fail-on-missing-field' = 'false',
  'json.ignore-parse-errors' = 'true'
);

CREATE TABLE MeterReference (
  ID_METER STRING,
  MEASURE_TYPE STRING, 
  UNIT STRING, 
  BUILDING STRING, 
  ROOM STRING, 
  AREA INTEGER, 
  LAST_UPDATE timestamp 
) WITH (
   'connector' = 'jdbc',
   'url' = 'jdbc:postgresql://datareference:5432/referencedata',
   'username' = 'postgres',
   'password' = 'password', 
   'table-name' = 'METER'
);

CREATE TABLE MeterWithDataReference (
  meterId STRING,
  corrMeterValue DOUBLE,
  measureType STRING, 
  unit STRING, 
  building STRING, 
  room STRING, 
  area INTEGER,
  ts TIMESTAMP,
  PRIMARY KEY (building) NOT ENFORCED
) WITH (
  'connector' = 'upsert-kafka',
  'topic' = 'output-meter-values-with-reference-data-from-table-api',
  'properties.bootstrap.servers' = 'kafka:9092',
  'properties.group.id' = 'consumer-group-table-api',
  'properties.num.partitions' = '10',
  'key.format' = 'raw',
  'value.format' = 'json'
);

INSERT INTO MeterWithDataReference
SELECT k.meterId, k.corrMeterValue, r.MEASURE_TYPE as measureType, r.UNIT as unit, r.BUILDING as building, r.ROOM as room, r.AREA as area, k.ts
FROM KafkaTable k 
LEFT JOIN MeterReference r ON k.meterId = r.ID_METER;