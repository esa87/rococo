package rococo.tests.grpc;

import grpc.rococo.UserdataServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import rococo.config.Config;
import rococo.jupiter.annotation.meta.GrpcTest;

@GrpcTest
public class BaseGrpcTest {
    protected static final Config CFG = Config.getInstance();


    protected static final Channel channel = ManagedChannelBuilder
            .forAddress(CFG.userGrpcAddress(), CFG.userGrpcPort())
            .usePlaintext()
            .build();

    protected static final UserdataServiceGrpc.UserdataServiceBlockingStub blockingStub =
            UserdataServiceGrpc.newBlockingStub(channel);

}
