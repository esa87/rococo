package rococo.domain;

import java.util.UUID;

public record Country(
        UUID id,
        String name
) {
}