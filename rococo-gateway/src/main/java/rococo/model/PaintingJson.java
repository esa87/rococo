package rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import grpc.rococo.ArtistResponse;
import grpc.rococo.CountryResponse;
import grpc.rococo.MuseumResponse;
import grpc.rococo.PaintingResponse;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaintingJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("title")
        String title,
        @JsonProperty("description")
        String description,
        @JsonProperty("artist")
        ArtistJson artist,
        @JsonProperty("museum")
        MuseumJson museum,
        @JsonProperty("content")
        String content
) {
    public static PaintingJson fromPaintingResponse(
            PaintingResponse paintingResponse,
            CountryResponse countryResponse,
            MuseumResponse museumResponse,
            ArtistResponse artistResponse) {
        return new PaintingJson(
                UUID.fromString(paintingResponse.getId()),
                paintingResponse.getTitle(),
                paintingResponse.getDescription(),
                new ArtistJson(
                        UUID.fromString(artistResponse.getId()),
                        artistResponse.getName(),
                        artistResponse.getBiography(),
                        artistResponse.getPhoto()
                ),
                new MuseumJson(
                        UUID.fromString(museumResponse.getId()),
                        museumResponse.getTitle(),
                        museumResponse.getDescription(),
                        new GeoJson(
                                museumResponse.getCity(),
                                new CountryJson(
                                        UUID.fromString(countryResponse.getId()),
                                        countryResponse.getName()
                                )
                        ),
                        museumResponse.getPhoto()
                ),
                paintingResponse.getContent()
        );
    }
}
