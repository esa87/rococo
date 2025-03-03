package rococo.domain;

import java.util.UUID;

public record User(
        UUID id,
        String username,
        String firstname,
        String lastname,
        byte[] avatar) {
}
