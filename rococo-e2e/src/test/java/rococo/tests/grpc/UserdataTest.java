package rococo.tests.grpc;

import grpc.rococo.UserResponse;
import grpc.rococo.UsernameRequest;
import org.junit.jupiter.api.Test;
import rococo.model.UserJson;

public class UserdataTest extends BaseGrpcTest {
    @Test
    void getCurrentUser() {
        UserResponse userResponse = blockingStub.user(UsernameRequest.newBuilder().setUsername("esa1").build());
        UserJson userJson = new UserJson(userResponse.getUsername());
        System.out.println(userJson);
    }

}
