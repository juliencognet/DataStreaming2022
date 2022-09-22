package com.cgi.datastream.conference.operations;

import com.cgi.datastream.conference.MeterValueWithReferenceData;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class SerializeMeterValueWithReferenceData implements MapFunction<MeterValueWithReferenceData, String> {

    ObjectMapper objectMapper;

    public SerializeMeterValueWithReferenceData(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public String map(MeterValueWithReferenceData meterValueWithReferenceData) throws Exception {
        return objectMapper.writeValueAsString(meterValueWithReferenceData);
    }
}
