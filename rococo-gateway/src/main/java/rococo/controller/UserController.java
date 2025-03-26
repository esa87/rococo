package rococo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rococo.model.UserJson;
import rococo.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserJson> user(@AuthenticationPrincipal Jwt jwt) {
        try {
            if (jwt == null) {
                log.warn("Unauthenticated user request");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
            }

            String username = jwt.getClaimAsString("sub");
            if (username == null || username.isBlank()) {
                log.error("JWT token missing 'sub' claim");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token: missing subject");
            }

            log.debug("Fetching user data for: {}", username);
            UserJson user = userService.userFindByName(username);
            return ResponseEntity.ok(user);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch user data", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve user information",
                    e
            );
        }
    }

    @PatchMapping()
    public ResponseEntity<UserJson> updateUser(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UserJson userJson) {
        try {
            if (jwt == null) {
                log.warn("Unauthenticated user update attempt");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
            }

            String username = jwt.getClaimAsString("sub");
            if (username == null || username.isBlank()) {
                log.error("JWT token missing 'sub' claim during update");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token: missing subject");
            }

            if (!username.equals(userJson.username())) {
                log.warn("User {} attempted to modify another user's data ({})", username, userJson.username());
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify another user's data");
            }

            log.info("Updating user profile for: {}", username);
            UserJson updatedUser = new UserJson(
                    userJson.id(),
                    username, // Используем username из JWT для безопасности
                    userJson.firstname(),
                    userJson.lastname(),
                    userJson.avatar()
            );

            UserJson result = userService.updateUser(updatedUser);
            return ResponseEntity.ok(result);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update user data", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to update user information",
                    e
            );
        }
    }
}
