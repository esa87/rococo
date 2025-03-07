package rococo.service;

import jakarta.annotation.Nonnull;
import rococo.domain.User;
import rococo.model.UserJson;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserJson userFindByName(@Nonnull String username);

    UserJson updateUser(@Nonnull UserJson userJson);


}