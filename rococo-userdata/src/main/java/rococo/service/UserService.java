package rococo.service;

import rococo.model.UserJson;
import javax.annotation.Nonnull;

public interface UserService {

    UserJson userFindByName(@Nonnull String username);

    UserJson updateUser(@Nonnull UserJson userJson);


}