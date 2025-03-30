package rococo.service;

import grpc.rococo.UserResponse;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rococo.service.client.UserGrpcClientService;
import rococo.model.UserJson;

import java.util.concurrent.CompletableFuture;

@Component
public class UserServiceToClientGrpc implements UserService {

    private final UserGrpcClientService userGrpcClientService;

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceToClientGrpc.class);

    @Autowired
    public UserServiceToClientGrpc(UserGrpcClientService userGrpcClientService) {
        this.userGrpcClientService = userGrpcClientService;
    }

    @Override
    public UserJson userFindByName(String username) {
        CompletableFuture<UserResponse> response = userGrpcClientService.getUser(username);
        return response.thenApply(UserJson::fromUserResponse).join();
    }


    @Override
    public UserJson updateUser(@Nonnull UserJson userJson) {
        CompletableFuture<UserResponse> response = userGrpcClientService.updateUser(userJson);
        return response.thenApply(UserJson::fromUserResponse).join();
    }
}
