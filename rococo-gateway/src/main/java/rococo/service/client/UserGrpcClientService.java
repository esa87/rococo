package rococo.service.client;

import grpc.rococo.UserRequest;
import grpc.rococo.UserResponse;
import grpc.rococo.UserdataServiceGrpc;
import grpc.rococo.UsernameRequest;
import org.springframework.stereotype.Service;
import rococo.model.UserJson;

import java.util.concurrent.CompletableFuture;

@Service
public class UserGrpcClientService {

    private final UserdataServiceGrpc.UserdataServiceFutureStub userdataServiceFutureStub;

    public UserGrpcClientService(UserdataServiceGrpc.UserdataServiceFutureStub userdataServiceFutureStub) {
        this.userdataServiceFutureStub = userdataServiceFutureStub;
    }

    // Метод для получения пользователя по имени
    public CompletableFuture<UserResponse> getUser(String username) {
        UsernameRequest request = UsernameRequest.newBuilder()
                .setUsername(username)
                .build();
       return CompletableFuture.supplyAsync(() -> {
            try {
                return userdataServiceFutureStub.user(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch user: " + e.getMessage(), e);
            }
        });
    }

    // Метод для обновления пользователя
    public CompletableFuture<UserResponse> updateUser(UserJson user) {
        UserRequest request = UserRequest.newBuilder()
                .setUsername(user.username())
                .setFirstname(user.firstname())
                .setLastname(user.lastname())
                .setAvatar(user.avatar())
                .build();
        return CompletableFuture.supplyAsync(() -> {
           try{
               return userdataServiceFutureStub.updateUser(request).get();
           } catch (Exception e) {
               throw new RuntimeException("Failed to fetch user: " + e.getMessage(),e);
           }
        });
    }
}
