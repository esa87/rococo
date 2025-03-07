package rococo.domain;

import java.util.UUID;

public record Artist(
        UUID id,
        String name,
        String biography,
        byte[] photo
) {
}
