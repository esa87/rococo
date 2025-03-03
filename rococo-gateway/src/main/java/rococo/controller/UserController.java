package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rococo.domain.User;
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

    @GetMapping("/all")
    public List<User> all() {
        return userService.allUsers();
    }

    @GetMapping("/current")
    public User currentUser(@RequestPart UUID userId) {
        return userService.userById(userId);
    }

    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        return  userService.addUser(user);
    }
}
