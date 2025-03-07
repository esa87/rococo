package rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeoJson(
        @JsonProperty("city")
        String city,
        @JsonProperty("country")
        CountryJson country
) {

}
