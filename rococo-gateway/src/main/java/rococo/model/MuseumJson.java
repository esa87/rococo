package rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import grpc.rococo.CountryResponse;
import grpc.rococo.MuseumResponse;

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

    public static MuseumJson fromMuseumResponse(MuseumResponse museum, CountryResponse country) {
        return new MuseumJson(
                UUID.fromString(museum.getId()),
                museum.getTitle(),
                museum.getDescription(),
                new GeoJson(
                        museum.getCity(),
                        new CountryJson(
                               UUID.fromString(country.getId()),
                                country.getName()
                        )
                ),
                museum.getPhoto()

        );
    }
}
