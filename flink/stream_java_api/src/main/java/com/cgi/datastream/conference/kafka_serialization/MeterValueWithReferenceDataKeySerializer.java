package com.cgi.datastream.conference.kafka_serialization;

import com.cgi.datastream.conference.MeterValueWithReferenceData;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class MeterValueWithReferenceDataKeySerializer implements SerializationSchema<MeterValueWithReferenceData>{
    ObjectMapper objectMapper;

    public MeterValueWithReferenceDataKeySerializer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(MeterValueWithReferenceData meterValueWithReferenceData) {
        return meterValueWithReferenceData.getBuilding().getBytes();
    }
}
