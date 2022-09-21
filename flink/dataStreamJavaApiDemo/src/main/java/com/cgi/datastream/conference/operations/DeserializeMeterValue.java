package com.cgi.datastream.conference.operations;

import com.cgi.datastream.conference.MeterValue;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class DeserializeMeterValue implements MapFunction<String, MeterValue> {

    ObjectMapper objectMapper;

    public DeserializeMeterValue(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public MeterValue map(String s) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(s, MeterValue.class);
    }
}
