package rococo.service;

import jakarta.annotation.Nonnull;
import rococo.model.UserJson;

public interface UserService {

    UserJson userFindByName(@Nonnull String username);

    UserJson updateUser(@Nonnull UserJson userJson);


}