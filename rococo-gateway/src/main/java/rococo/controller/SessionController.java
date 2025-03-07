package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import rococo.domain.Session;
import rococo.domain.User;
import rococo.service.UserService;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    public SessionController() {
    }

    @GetMapping("")
    public Session session(@AuthenticationPrincipal Jwt jwt) {
        ZonedDateTime now = ZonedDateTime.now();
        return new Session(
                jwt.getClaimAsString("sub"),
                Session.createIssuedAt(now),
                Session.createExpiresAt(now)
        );
    }

}
