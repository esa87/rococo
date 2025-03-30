package rococo.service;

import rococo.model.UserJson;

public interface UserClient {
    UserJson createUser(String username, String password);

}
