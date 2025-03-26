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
        @JsonProperty("geo")
        GeoJson geo,
        @JsonProperty("photo")
        String photo

) {

    public static MuseumJson fromMuseumEntity(MuseumEntity museumEntity) {
        return new MuseumJson(
                museumEntity.getId(),
                museumEntity.getTitle(),
                museumEntity.getDescription(),
                new GeoJson(
                        museumEntity.getCity(),
                        new CountryJson(
                                museumEntity.getCountry().getId(),
                                museumEntity.getCountry().getName()
                        )
                ),
                museumEntity.getPhoto() != null && museumEntity.getPhoto().length > 0 ? new String(museumEntity.getPhoto(), StandardCharsets.UTF_8) : null

        );
    }
}
