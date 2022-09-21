package com.cgi.datastream.conference.operations;

import com.cgi.datastream.conference.Meter;
import com.cgi.datastream.conference.MeterValue;
import com.cgi.datastream.conference.MeterValueWithReferenceData;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class JoinMeterReferenceAndMeterValues implements CoFlatMapFunction<MeterValue, Meter[], MeterValueWithReferenceData> {

    final HashMap<String,Meter> meters = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(JoinMeterReferenceAndMeterValues.class);

    @Override
    public void flatMap1(MeterValue meterValue, Collector<MeterValueWithReferenceData> collector) {
        if (meters.containsKey(meterValue.getMeterId())) {
            Meter currentMeter = meters.get(meterValue.getMeterId());
            MeterValueWithReferenceData meterValueWithReferenceData = new MeterValueWithReferenceData();
            meterValueWithReferenceData.setMeterId(currentMeter.getIdMeter());
            meterValueWithReferenceData.setArea(currentMeter.getArea());
            meterValueWithReferenceData.setBuilding(currentMeter.getBuilding());
            meterValueWithReferenceData.setMeasureType(currentMeter.getMeasureType());
            meterValueWithReferenceData.setRoom(currentMeter.getRoom());
            meterValueWithReferenceData.setMeterTimestamp(meterValue.getMeterTimestamp());
            meterValueWithReferenceData.setMeterValue(meterValue.getMeterValue());
            meterValueWithReferenceData.setUnit(currentMeter.getUnit());
            collector.collect(meterValueWithReferenceData);
        } else {
            LOG.error("Cannot retrieve meter with ID {}", meterValue.getMeterId());
        }

    }

    @Override
    public void flatMap2(Meter[] meter, Collector<MeterValueWithReferenceData> collector) {
        for (Meter meterElement : meter) {
            meters.put(meterElement.getIdMeter(),meterElement);
        }
    }
}
