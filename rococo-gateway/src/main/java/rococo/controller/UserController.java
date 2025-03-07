package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import rococo.domain.User;
import rococo.model.UserJson;
import rococo.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public UserJson user(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("sub");
        return userService.userFindByName(username);
    }

    @PatchMapping()
    public UserJson updateUser(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UserJson userJson) {
        UserJson user= new UserJson(
                userJson.id(),
                jwt.getClaimAsString("sub"),
                userJson.firstname(),
                userJson.lastname(),
                userJson.avatar()
        );
        return userService.updateUser(user);
    }

}
