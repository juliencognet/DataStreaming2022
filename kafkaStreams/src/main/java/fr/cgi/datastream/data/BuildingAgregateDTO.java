package fr.cgi.datastream.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@Generated
public class BuildingAgregateDTO {

    @JsonProperty("result")
    private Float result;

    @JsonProperty("building")
    private String building;

    public BuildingAgregateDTO() {
        this.result = 0f;
    }
}
