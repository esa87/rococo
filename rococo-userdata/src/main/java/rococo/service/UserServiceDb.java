package rococo.service;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rococo.data.UserEntity;
import rococo.data.UserRepository;
import rococo.model.UserJson;

@Service
public class UserServiceDb implements UserService {

    private final UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceDb.class);

    @Autowired
    public UserServiceDb(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserJson userFindByName(@Nonnull String username) {
        return userRepository.findByUsername(username)
                .map(ue ->  UserJson.fromUserEntity(ue)).orElse(new UserJson(null, null, null, null, null));
    }

    @Override
    @Transactional
    public UserJson updateUser(@Nonnull UserJson userJson) {
        userRepository.findByUsername(userJson.username())
                .orElseThrow(() -> new EntityNotFoundException("User not found from username: "+userJson.username()));
            UserEntity entity = UserEntity.fromUserJson(userJson);
            userRepository.save(entity);
            return UserJson.fromUserEntity(entity);
    }

    @Transactional
    @KafkaListener(topics = "users", groupId = "userdata")
    public void listener(@Payload UserJson user, ConsumerRecord<String, UserJson> cr) {
        userRepository.findByUsername(user.username())
                .ifPresentOrElse(
                        u -> LOG.info("### User already exist in DB, kafka event will be skipped: {}", cr.toString()),
                        () -> {
                            LOG.info("### Kafka consumer record: {}", cr.toString());

                            UserEntity userDataEntity = new UserEntity();
                            userDataEntity.setUsername(user.username());
                            UserEntity userEntity = userRepository.save(userDataEntity);

                            LOG.info(
                                    "### User '{}' successfully saved to database with id: {}",
                                    user.username(),
                                    userEntity.getId()
                            );
                        }
                );
    }
}
