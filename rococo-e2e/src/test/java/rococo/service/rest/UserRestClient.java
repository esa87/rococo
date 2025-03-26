package rococo.service.rest;

import rococo.api.AuthUserApiClient;

import rococo.model.TestData;
import rococo.model.UserJson;
import rococo.service.UserClient;
import io.qameta.allure.Step;
import org.apache.commons.lang3.time.StopWatch;
import rococo.service.grpc.UserdataGrpcClient;

import java.util.concurrent.TimeUnit;

public class UserRestClient implements UserClient {

    private final AuthUserApiClient authUserApiClient = new AuthUserApiClient();
    private final UserdataGrpcClient userdataGrpcClient = new UserdataGrpcClient();

    @Override
    @Step("Create user using API")
    public UserJson createUser(String username, String password) {
        authUserApiClient.requestRegisterForm();
        authUserApiClient.registerUser(username, password);
        StopWatch sw = new StopWatch();
        sw.start();
        final long limitTime = 30000L;
        UserJson user;
        while (sw.getTime(TimeUnit.MILLISECONDS) < limitTime) {
            user = userdataGrpcClient.getCurrentUser(username);
            if (user != null && user.id() != null) {
               return user.addTestData(new TestData(password));
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("User creation timed out after 3000 milliseconds");
    }


}
