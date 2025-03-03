package rococo.service;

import rococo.domain.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> allUsers();

    User userById(UUID userId);

    User addUser(User user);
}