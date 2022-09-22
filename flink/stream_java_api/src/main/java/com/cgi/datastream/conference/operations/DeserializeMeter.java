package com.cgi.datastream.conference.operations;

import com.cgi.datastream.conference.Meter;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class DeserializeMeter implements MapFunction<String, Meter[]> {

    ObjectMapper objectMapper;

    public DeserializeMeter(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public Meter[] map(String s) throws Exception {
        return objectMapper.readValue(s, Meter[].class);
    }
}
