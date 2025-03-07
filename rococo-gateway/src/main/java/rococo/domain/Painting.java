package rococo.domain;

import java.util.List;
import java.util.UUID;

public record Painting(
        UUID id,
        String title,
        String description,
        UUID artist_id,
        UUID museum_id,
        byte[] content
) {
}
