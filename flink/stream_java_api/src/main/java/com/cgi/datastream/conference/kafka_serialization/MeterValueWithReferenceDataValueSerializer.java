package com.cgi.datastream.conference.kafka_serialization;

import com.cgi.datastream.conference.MeterValueWithReferenceData;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class MeterValueWithReferenceDataValueSerializer implements SerializationSchema<MeterValueWithReferenceData>{
    ObjectMapper objectMapper;

    public MeterValueWithReferenceDataValueSerializer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(MeterValueWithReferenceData meterValueWithReferenceData) {
        try {
            return objectMapper.writeValueAsBytes(meterValueWithReferenceData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
