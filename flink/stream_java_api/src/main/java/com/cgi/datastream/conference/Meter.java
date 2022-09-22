package com.cgi.datastream.conference;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meter {
    @JsonProperty("id_meter")
    public String idMeter;
    @JsonProperty("measure_type")
    public String measureType;

    @JsonProperty("unit")
    public String unit;

    @JsonProperty("building")
    public String building;

    @JsonProperty("room")
    public String room;

    @JsonProperty("area")
    public int area;

    public String getIdMeter() {
        return idMeter;
    }

    public void setIdMeter(String idMeter) {
        this.idMeter = idMeter;
    }

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
}
