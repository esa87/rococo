package rococo.service.grpc;

import grpc.rococo.*;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import rococo.config.Config;
import rococo.jupiter.annotation.meta.GrpcTest;

@GrpcTest
public class BaseGrpc {
    protected static final Config CFG = Config.getInstance();


    protected static final Channel channelUserdata = ManagedChannelBuilder
            .forAddress(CFG.userGrpcAddress(), CFG.userGrpcPort())
            .usePlaintext()
            .build();
    protected static final Channel channelArtist = ManagedChannelBuilder
            .forAddress(CFG.artistGrpcAddress(), CFG.artistGrpcPort())
            .usePlaintext()
            .build();

    protected static final Channel channelMuseum = ManagedChannelBuilder
            .forAddress(CFG.museumGrpcAddress(), CFG.museumGrpcPort())
            .usePlaintext()
            .build();

    protected static final Channel channelCountry = ManagedChannelBuilder
            .forAddress(CFG.countryGrpcAddress(), CFG.countryGrpcPort())
            .usePlaintext()
            .build();

    protected static final Channel channelPainting = ManagedChannelBuilder
            .forAddress(CFG.paintingGrpcAddress(), CFG.paintingGrpcPort())
            .usePlaintext()
            .build();


    protected static final UserdataServiceGrpc.UserdataServiceBlockingStub userdataBlockingStub =
            UserdataServiceGrpc.newBlockingStub(channelUserdata);

    protected static final ArtistServiceGrpc.ArtistServiceBlockingStub artistBlockingStub =
            ArtistServiceGrpc.newBlockingStub(channelArtist);


    protected static final MuseumServiceGrpc.MuseumServiceBlockingStub museumBlockingStub =
            MuseumServiceGrpc.newBlockingStub(channelMuseum);

    protected static final CountryServiceGrpc.CountryServiceBlockingStub countryBlockingStub =
            CountryServiceGrpc.newBlockingStub(channelCountry);

    protected static final PaintingServiceGrpc.PaintingServiceBlockingStub paintingBlockingStub =
            PaintingServiceGrpc.newBlockingStub(channelPainting);

}
