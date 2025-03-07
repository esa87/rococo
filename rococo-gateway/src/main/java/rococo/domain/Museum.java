package rococo.domain;

import java.util.UUID;

public record Museum(
        UUID id,
        String title,
        String description,
        String city,
        byte [] photo,
        UUID country_id
) {
}
