package com.cgi.datastream.conference;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

public class MeterValueWithReferenceData {
    String meterTimestamp;
    String meterId;

    String meterValue;

    String measureType;

    String unit;

    String building;

    String room;

    int area;

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getMeterTimestamp() {
        return meterTimestamp;
    }

    public void setMeterTimestamp(String meterTimestamp) {
        this.meterTimestamp = meterTimestamp;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getMeterValue() {
        return meterValue;
    }

    public void setMeterValue(String meterValue) {
        this.meterValue = meterValue;
    }
}
