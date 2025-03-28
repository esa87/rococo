package rococo.service;

import grpc.rococo.UserRequest;
import grpc.rococo.UserResponse;
import grpc.rococo.UserdataServiceGrpc;
import grpc.rococo.UsernameRequest;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;
import rococo.model.UserJson;

@Service
public class GrpcUserService extends UserdataServiceGrpc.UserdataServiceImplBase {

    private final UserService userService;

    public GrpcUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void user(UsernameRequest request, StreamObserver<UserResponse> responseObserver) {
        UserJson userJson = userService.userFindByName(request.getUsername());

        responseObserver.onNext(
                UserResponse.newBuilder()
                        .setId(userJson.id() == null
                                ? ""
                                : userJson.id().toString())
                        .setUsername(userJson.username() == null
                                ? ""
                                : userJson.username())
                        .setFirstname(userJson.firstname() == null
                                ? ""
                                : userJson.firstname())
                        .setLastname(userJson.lastname() == null
                                ? ""
                                : userJson.lastname())
                        .setAvatar(userJson.avatar() == null
                                ? ""
                                : userJson.avatar())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {

        UserJson userJson = userService.updateUser(new UserJson(
                userService.userFindByName(request.getUsername()).id(),
                request.getUsername(),
                request.getFirstname(),
                request.getLastname(),
                request.getAvatar()
        ));
        responseObserver.onNext(
                UserResponse.newBuilder()
                        .setId(userJson.id().toString())
                        .setUsername(userJson.username())
                        .setFirstname(userJson.firstname() == null
                                ? ""
                                : userJson.firstname())
                        .setLastname(userJson.lastname() == null
                                ? ""
                                : userJson.lastname())
                        .setAvatar(userJson.avatar() == null
                                ? ""
                                : userJson.avatar())
                        .build()
        );
        responseObserver.onCompleted();
    }
}
