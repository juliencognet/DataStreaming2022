package fr.cgi.datastream.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Generated
public class BuildingDTO {

    @JsonProperty("meterId")
    private String meterId;

    @JsonProperty("corrMeterValue")
    private Float corrMeterValue;

    @JsonProperty("room")
    private String room;

    @JsonProperty("measureType")
    private String measureType;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("building")
    private String building;


    @JsonProperty("area")
    private Float area;

    @JsonProperty("ts")
    private String ts;


}
