package rococo.service;

import grpc.rococo.UserResponse;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rococo.service.client.UserGrpcClientService;
import rococo.model.UserJson;
import rococo.service.exception.GrpcExceptionUtil;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
public class UserServiceToClientGrpc implements UserService {

    private final UserGrpcClientService userGrpcClientService;

    @Autowired
    public UserServiceToClientGrpc(UserGrpcClientService userGrpcClientService) {
        this.userGrpcClientService = userGrpcClientService;
    }

    @Override
    public UserJson userFindByName(String username) {
        try {
            UserResponse response = userGrpcClientService.getUser(username).join();
            return UserJson.fromUserResponse(response);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e);
        }
    }

    @Override
    public UserJson updateUser(@Nonnull UserJson userJson) {
        try {
            UserResponse response = userGrpcClientService.updateUser(userJson).join();
            return UserJson.fromUserResponse(response);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, userJson.id());
        }
    }
}
