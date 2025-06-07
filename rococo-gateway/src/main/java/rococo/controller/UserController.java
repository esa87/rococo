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
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserJson> user(@AuthenticationPrincipal Jwt jwt) {
        UserJson user = userService.userFindByName(jwt.getClaimAsString("sub"));
        return ResponseEntity.ok(user);
    }

    @PatchMapping()
    public ResponseEntity<UserJson> updateUser(
            @RequestBody UserJson userJson) {

        UserJson updatedUser = new UserJson(
                userJson.id(),
                userJson.username(),
                userJson.firstname(),
                userJson.lastname(),
                userJson.avatar()
        );

        UserJson result = userService.updateUser(updatedUser);
        return ResponseEntity.ok(result);
    }
}
