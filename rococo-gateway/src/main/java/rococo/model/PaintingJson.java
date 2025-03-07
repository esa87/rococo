package rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import rococo.data.PaintingEntity;

import java.nio.charset.StandardCharsets;
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
    public static PaintingJson fromPaintingEntity(PaintingEntity entity) {
        return new PaintingJson(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                new ArtistJson(
                        entity.getArtistEntity().getId(),
                        entity.getArtistEntity().getName(),
                        entity.getArtistEntity().getBiography(),
                        entity.getArtistEntity().getPhoto() != null && entity.getArtistEntity().getPhoto().length > 0 ? new String(entity.getArtistEntity().getPhoto(), StandardCharsets.UTF_8) : null
                ),
                new MuseumJson(
                        entity.getMuseumEntity().getId(),
                        entity.getMuseumEntity().getTitle(),
                        entity.getMuseumEntity().getDescription(),
                        new GeoJson(
                                entity.getMuseumEntity().getCity(),
                                new CountryJson(
                                        entity.getMuseumEntity().getCountry().getId(),
                                        entity.getMuseumEntity().getCountry().getName()
                                )
                        ),
                        entity.getMuseumEntity().getPhoto()  != null && entity.getMuseumEntity().getPhoto().length > 0 ? new String(entity.getMuseumEntity().getPhoto(), StandardCharsets.UTF_8) : null
                ),
                entity.getContent() != null && entity.getContent().length > 0 ? new String(entity.getContent(), StandardCharsets.UTF_8) : null
        );
    }
}
