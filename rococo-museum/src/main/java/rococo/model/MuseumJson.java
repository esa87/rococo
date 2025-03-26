package rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import rococo.data.MuseumEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MuseumJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("title")
        String title,
        @JsonProperty("description")
        String description,
        @JsonProperty("city")
        String city,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("countryId")
        UUID countryId

) {

    public static MuseumJson fromMuseumEntity(MuseumEntity museumEntity) {
        return new MuseumJson(
                museumEntity.getId(),
                museumEntity.getTitle(),
                museumEntity.getDescription(),
                museumEntity.getCity(),
                museumEntity.getPhoto() != null && museumEntity.getPhoto().length > 0 ? new String(museumEntity.getPhoto(), StandardCharsets.UTF_8) : null,
                museumEntity.getCountryId()
        );
    }
}
