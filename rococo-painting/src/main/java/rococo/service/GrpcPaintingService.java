package rococo.service;

import grpc.rococo.*;
import io.grpc.stub.StreamObserver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rococo.model.PaintingJson;

import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class GrpcPaintingService extends PaintingServiceGrpc.PaintingServiceImplBase {

    private final PaintingService paintingService;

    public GrpcPaintingService(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @Override
    public void allPaintings(AllPaintingsRequest request, StreamObserver<PaintingsPageResponse> responseObserver) {
        Pageable pageable = PageRequest.of(request.getPageable().getPage(), request.getPageable().getSize());

        Page<PaintingJson> paintings = paintingService.allPaintings(request.getSearchQuery(), pageable);

        PaintingsPageResponse response = PaintingsPageResponse.newBuilder()
                .addAllPaintings(
                        paintings.getContent().stream()
                                .map(painting -> PaintingResponse.newBuilder()
                                        .setId(painting.id().toString())
                                        .setTitle(painting.title())
                                        .setDescription(painting.description())
                                        .setArtistId(painting.artistId().toString())
                                        .setMuseumId(painting.museumId().toString())
                                        .setContent(painting.content()==null?"":painting.content())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void allPaintingsForArtist(PaintingsForArtistPageRequest request, StreamObserver<PaintingsPageResponse> responseObserver) {
        Pageable pageable = PageRequest.of(request.getPaintingsPageRequest().getPage(), request.getPaintingsPageRequest().getSize());

        Page<PaintingJson> paintings = paintingService.allPaintingsForArtist(UUID.fromString(request.getArtistId()), pageable);

        PaintingsPageResponse response = PaintingsPageResponse.newBuilder()
                .addAllPaintings(
                        paintings.getContent().stream()
                                .map(painting -> PaintingResponse.newBuilder()
                                        .setId(painting.id().toString())
                                        .setTitle(painting.title())
                                        .setDescription(painting.description())
                                        .setArtistId(painting.artistId().toString())
                                        .setMuseumId(painting.museumId().toString())
                                        .setContent(painting.content())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void paintingById(PaintingForIdPageRequest request, StreamObserver<PaintingResponse> responseObserver) {
        PaintingJson painting = paintingService.paintingById(UUID.fromString(request.getId()));

        responseObserver.onNext(
                PaintingResponse.newBuilder()
                        .setId(painting.id().toString())
                        .setTitle(painting.title())
                        .setDescription(painting.description())
                        .setArtistId(painting.artistId().toString())
                        .setMuseumId(painting.museumId().toString())
                        .setContent(painting.content())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void addPainting(PaintingRequest request, StreamObserver<PaintingResponse> responseObserver) {
        PaintingJson painting = paintingService.addPainting(
                new PaintingJson(
                        null,
                        request.getTitle(),
                        request.getDescription(),
                        UUID.fromString(request.getArtistId()),
                        UUID.fromString(request.getMuseumId()),
                        request.getContent()

                )
        );

        responseObserver.onNext(
                PaintingResponse.newBuilder()
                        .setId(painting.id().toString())
                        .setTitle(painting.title())
                        .setDescription(painting.description())
                        .setArtistId(painting.artistId().toString())
                        .setMuseumId(painting.museumId().toString())
                        .setContent(painting.content()==null?"":painting.content())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void updatePainting(PaintingUpdateRequest request, StreamObserver<PaintingResponse> responseObserver) {
        PaintingJson painting = paintingService.updatePainting(
                new PaintingJson(
                        UUID.fromString(request.getId()),
                        request.getTitle(),
                        request.getDescription(),
                        UUID.fromString(request.getArtistId()),
                        UUID.fromString(request.getMuseumId()),
                        request.getContent()
                )
        );
        responseObserver.onNext(
                PaintingResponse.newBuilder()
                        .setId(painting.id().toString())
                        .setTitle(painting.title())
                        .setDescription(painting.description())
                        .setArtistId(painting.artistId().toString())
                        .setMuseumId(painting.museumId().toString())
                        .setContent(painting.content())
                        .build()
        );
        responseObserver.onCompleted();
    }
}
