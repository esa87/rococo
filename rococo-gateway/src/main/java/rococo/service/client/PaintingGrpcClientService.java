package rococo.service.client;

import grpc.rococo.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rococo.model.PaintingJson;

import java.util.concurrent.CompletableFuture;

@Service
public class PaintingGrpcClientService {
    private final PaintingServiceGrpc.PaintingServiceFutureStub paintingServiceFutureStub;

    public PaintingGrpcClientService(PaintingServiceGrpc.PaintingServiceFutureStub paintingServiceFutureStub) {
        this.paintingServiceFutureStub = paintingServiceFutureStub;
    }

    // Получить все картины с пагинацией
    public CompletableFuture<PaintingsPageResponse> getAllPaintings(String searchQuery, Pageable pageable) {
        AllPaintingsRequest request = AllPaintingsRequest.newBuilder()
                .setSearchQuery(searchQuery==null?"":searchQuery)
                .setPageable(PaintingsPageRequest.newBuilder()
                        .setPage(pageable.getPageNumber())
                        .setSize(pageable.getPageSize())
                        .build())
                .build();

        return CompletableFuture.supplyAsync(() -> {
                    try {

                        return paintingServiceFutureStub.allPaintings(request).get();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to fetch paintings: " + e.getMessage(), e);
                    }
                }

        );

    }

    // Получить все картины для конкретного художника с пагинацией
    public CompletableFuture<PaintingsPageResponse> getAllPaintingsForArtist(String artistId, Pageable pageable) {
        PaintingsForArtistPageRequest request = PaintingsForArtistPageRequest.newBuilder()
                .setArtistId(artistId)
                .setPaintingsPageRequest(PaintingsPageRequest.newBuilder()
                        .setPage(pageable.getPageNumber())
                        .setSize(pageable.getPageSize())
                        .build())
                .build();

        return CompletableFuture.supplyAsync(() -> {
            try {
                return paintingServiceFutureStub.allPaintingsForArtist(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch paintings for artist: " + e.getMessage(), e);
            }
        });

    }

    // Получить картину по ID
    public CompletableFuture<PaintingResponse> getPaintingById(String id) {
        PaintingForIdPageRequest request = PaintingForIdPageRequest.newBuilder()
                .setId(id)
                .build();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return paintingServiceFutureStub.paintingById(request).get();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to fetch painting by ID: " + e.getMessage(), e);
                    }
                }
        );


    }

    // Добавить новую картину
    public CompletableFuture<PaintingResponse> addPainting(PaintingJson painting) {
        PaintingRequest request = PaintingRequest.newBuilder()
                .setTitle(painting.title())
                .setDescription(painting.description())
                .setArtistId(painting.artist().id().toString())
                .setMuseumId(painting.museum().id().toString())
                .setContent(painting.content())
                .build();

        return CompletableFuture.supplyAsync(() -> {
            try {
                return paintingServiceFutureStub.addPainting(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to add painting: " + e.getMessage(), e);
            }
        });
    }

    // Обновить существующую картину
    public CompletableFuture<PaintingResponse> updatePainting(PaintingJson painting) {
        PaintingUpdateRequest request = PaintingUpdateRequest.newBuilder()
                .setId(painting.id().toString())
                .setTitle(painting.title())
                .setDescription(painting.description())
                .setArtistId(painting.artist().id().toString())
                .setMuseumId(painting.museum().id().toString())
                .setContent(painting.content())
                .build();

        return CompletableFuture.supplyAsync(() -> {
            try {
                return paintingServiceFutureStub.updatePainting(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to update painting: " + e.getMessage(), e);
            }
        });
    }
}
