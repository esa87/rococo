package rococo.service;

import grpc.rococo.*;
import io.grpc.stub.StreamObserver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import net.devh.boot.grpc.server.service.GrpcService;
import rococo.model.ArtistJson;

import java.util.UUID;
import java.util.stream.Collectors;

@GrpcService
public class GrpcArtistService extends ArtistServiceGrpc.ArtistServiceImplBase {

    private final ArtistService artistService;

    public GrpcArtistService(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    public void allArtist(AllArtistRequest request, StreamObserver<ArtistsPageResponse> responseObserver) {
        Pageable pageable = PageRequest.of(request.getPageable().getPage(), request.getPageable().getSize());
        Page<ArtistJson> artists = artistService.allArtist(request.getSearchQuery(), pageable);

        ArtistsPageResponse response = ArtistsPageResponse.newBuilder()
                .addAllArtists(
                        artists.getContent().stream()
                                .map(artist -> ArtistResponse.newBuilder()
                                        .setId(artist.id().toString())
                                        .setName(artist.name())
                                        .setBiography(artist.biography())
                                        .setPhoto(artist.photo() == null ? "" : artist.photo())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void artistById(ArtistsIdRequest request, StreamObserver<ArtistResponse> responseObserver) {
        ArtistJson artist = artistService.artistById(UUID.fromString(request.getId()));

        responseObserver.onNext(
                ArtistResponse.newBuilder()
                        .setId(artist.id().toString())
                        .setName(artist.name())
                        .setBiography(artist.biography())
                        .setPhoto(artist.photo() == null
                                ? ""
                                : artist.photo())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void addArtist(ArtistRequest request, StreamObserver<ArtistResponse> responseObserver) {
        ArtistJson artist = artistService.addArtist(
                new ArtistJson(
                        null,
                        request.getName(),
                        request.getBiography(),
                        request.getPhoto()
                )
        );

        responseObserver.onNext(
                ArtistResponse.newBuilder()
                        .setId(artist.id().toString())
                        .setName(artist.name())
                        .setBiography(artist.biography())
                        .setPhoto(artist.photo() == null
                                ? ""
                                : artist.photo())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void updateArtist(ArtistUpdateRequest request, StreamObserver<ArtistResponse> responseObserver) {
        ArtistJson artist = artistService.updateArtist(
                new ArtistJson(
                        UUID.fromString(request.getId()),
                        request.getName(),
                        request.getBiography(),
                        request.getPhoto()
                )
        );

        responseObserver.onNext(
                ArtistResponse.newBuilder()
                        .setId(artist.id().toString())
                        .setName(artist.name())
                        .setBiography(artist.biography())
                        .setPhoto(artist.photo() == null
                                ? ""
                                : artist.photo())
                        .build()
        );
        responseObserver.onCompleted();
    }
}
