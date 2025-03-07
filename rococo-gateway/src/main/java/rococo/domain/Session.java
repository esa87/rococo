package rococo.domain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public record Session(
        String username,
        String issuedAt,
        String expiresAt
) {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");

    public static String createIssuedAt(ZonedDateTime now) {
        String issuedAt = now.format(formatter);
        return issuedAt;
    }

    public static String createExpiresAt(ZonedDateTime now) {
        String expiresAt = now.plusHours(1).format(formatter);
        return expiresAt;
    }

}
