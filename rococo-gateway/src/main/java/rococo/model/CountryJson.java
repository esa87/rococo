package rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import grpc.rococo.CountryResponse;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CountryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name
) {

        public static CountryJson fromCountryResponse(CountryResponse response) {
                return new CountryJson(
                        UUID.fromString(response.getId()),
                        response.getName()
                );
        }
}