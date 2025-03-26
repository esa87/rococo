package rococo.service.grpc;

import grpc.rococo.ArtistRequest;
import grpc.rococo.ArtistResponse;
import grpc.rococo.UserResponse;
import grpc.rococo.UsernameRequest;
import rococo.model.ArtistJson;
import rococo.model.UserJson;
import rococo.service.ArtistClient;

import java.util.UUID;

public class ArtistGrpcClient extends BaseGrpc implements ArtistClient {


    @Override
    public ArtistJson addArtist(ArtistJson artist) {
        ArtistResponse response = artistBlockingStub.addArtist(
                ArtistRequest.newBuilder()
                        .setName(artist.name())
                        .setBiography(artist.biography())
                        .setPhoto(artist.photo())
                        .build()
        );


        return new ArtistJson(
                UUID.fromString(response.getId()),
                response.getName(),
                response.getBiography(),
                response.getPhoto()
        );
    }
}
