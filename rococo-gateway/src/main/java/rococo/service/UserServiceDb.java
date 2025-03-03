package rococo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rococo.data.UserEntity;
import rococo.data.UserRepository;
import rococo.domain.User;

import java.util.List;
import java.util.UUID;

@Component
public class UserServiceDb implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceDb(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> allUsers() {
        return userRepository.findAll()
                .stream()
                .map(ue -> {
                    return new User(
                            ue.getId(),
                            ue.getUsername(),
                            ue.getFirstname(),
                            ue.getLastname(),
                            ue.getAvatar()
                    );
                }).toList();
    }

    @Override
    public User userById(UUID userId) {
        return userRepository.findById(userId)
                .map(ue -> new User(
                        ue.getId(),
                        ue.getUsername(),
                        ue.getFirstname(),
                        ue.getLastname(),
                        ue.getAvatar()
                )).orElseThrow(() -> new RuntimeException("User not fount this id: " + userId));
    }

    @Override
    public User addUser(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.username());
        UserEntity resultUser = userRepository.save(userEntity);
        return new User(
                resultUser.getId(),
                resultUser.getUsername(),
                resultUser.getFirstname(),
                resultUser.getLastname(),
                resultUser.getAvatar()
        );
    }
}
