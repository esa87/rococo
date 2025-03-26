package rococo.service.grpc;

import grpc.rococo.UserRequest;
import grpc.rococo.UserResponse;
import grpc.rococo.UsernameRequest;
import rococo.model.UserJson;

import java.util.UUID;

public class UserdataGrpcClient extends BaseGrpc  {

    public UserJson getCurrentUser(String username) {
        UserResponse userResponse = userdataBlockingStub.user(UsernameRequest.newBuilder().setUsername(username).build());
       return  new UserJson(
               UUID.fromString(userResponse.getId()),
               userResponse.getUsername()
       );
    }

    public UserJson editUser(UserJson user){
        UserResponse userResponse = userdataBlockingStub.updateUser(
                UserRequest.newBuilder()
                        .setUsername(user.username())
                        .setFirstname(user.firstname())
                        .setLastname(user.lastname())
                        .setAvatar(user.avatar())
                        .build()
        );

        return  new UserJson(
                UUID.fromString(userResponse.getId()),
                userResponse.getUsername(),
                userResponse.getFirstname(),
                userResponse.getLastname(),
                userResponse.getAvatar(),
                null
        );
    }



}
