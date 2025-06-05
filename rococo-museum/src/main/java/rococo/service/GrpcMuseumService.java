package rococo.service;

import grpc.rococo.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rococo.model.MuseumJson;

import java.util.UUID;
import java.util.stream.Collectors;

@GrpcService
public class GrpcMuseumService extends MuseumServiceGrpc.MuseumServiceImplBase {

    private final MuseumService museumService;

    public GrpcMuseumService(MuseumService museumService) {
        this.museumService = museumService;
    }

    @Override
    public void allMuseums(AllMuseumsRequest request, StreamObserver<MuseumsPageResponse> responseObserver) {
        Pageable pageable = PageRequest.of(request.getPageable().getPage(), request.getPageable().getSize());

        Page<MuseumJson> museumJsons = museumService.allMuseums(request.getSearchQuery(), pageable);

        MuseumsPageResponse response = MuseumsPageResponse.newBuilder()
                .addAllMuseums(
                        museumJsons.getContent().stream()
                                .map(museum -> MuseumResponse.newBuilder()
                                        .setId(museum.id().toString())
                                        .setTitle(museum.title())
                                        .setDescription(museum.description())
                                        .setCity(museum.city())
                                        .setPhoto(museum.photo()==null?"":museum.photo())
                                        .setCountryId(museum.countryId().toString())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .setTotalPages(museumJsons.getTotalPages())
                .setTotalElements(museumJsons.getTotalElements())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void museumById(MuseumsIdRequest request, StreamObserver<MuseumResponse> responseObserver) {
        MuseumJson museum = museumService.museumById(UUID.fromString(request.getId()));

        responseObserver.onNext(
                MuseumResponse.newBuilder()
                        .setId(museum.id().toString())
                        .setTitle(museum.title())
                        .setDescription(museum.description())
                        .setCity(museum.city())
                        .setPhoto(museum.photo()==null?"":museum.photo())
                        .setCountryId(museum.countryId().toString())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void addMuseum(MuseumRequest request, StreamObserver<MuseumResponse> responseObserver) {
        MuseumJson museumJson = museumService.addMuseum(
                new MuseumJson(
                        null,
                        request.getTitle(),
                        request.getDescription(),
                        request.getCity(),
                        request.getPhoto(),
                        UUID.fromString(request.getCountryId())

                )
        );

        responseObserver.onNext(
                MuseumResponse.newBuilder()
                        .setId(museumJson.id().toString())
                        .setTitle(museumJson.title())
                        .setDescription(museumJson.description())
                        .setCity(museumJson.city())
                        .setPhoto(museumJson.photo()==null?"":museumJson.photo())
                        .setCountryId(museumJson.countryId().toString())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void updateMuseum(MuseumUpdateRequest request, StreamObserver<MuseumResponse> responseObserver) {
        MuseumJson museumJson = museumService.updateMuseum(
                new MuseumJson(
                        UUID.fromString(request.getId()),
                        request.getTitle(),
                        request.getDescription(),
                        request.getCity(),
                        request.getPhoto(),
                        UUID.fromString(request.getCountryId())
                )
        );

        responseObserver.onNext(
                MuseumResponse.newBuilder()
                        .setId(museumJson.id().toString())
                        .setTitle(museumJson.title())
                        .setDescription(museumJson.description())
                        .setCity(museumJson.city())
                        .setPhoto(museumJson.photo())
                        .setCountryId(museumJson.countryId().toString())
                        .build()
        );
        responseObserver.onCompleted();
    }
}
