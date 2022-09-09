package com.cgi.datastream.conference;

public class MeterValueWithReferenceData {
    String meterTimestamp;
    String meterId;
    String meterName;
    String meterValue;

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

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getMeterValue() {
        return meterValue;
    }

    public void setMeterValue(String meterValue) {
        this.meterValue = meterValue;
    }
}
