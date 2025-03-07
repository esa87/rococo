package rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import rococo.data.CountryEntity;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CountryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name
) {

        public static CountryJson fromCountryEntity(CountryEntity countryEntity) {
                return new CountryJson(
                        countryEntity.getId(),
                        countryEntity.getName()
                );
        }
}