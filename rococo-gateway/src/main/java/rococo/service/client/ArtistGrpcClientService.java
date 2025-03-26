package rococo.service.client;

import grpc.rococo.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rococo.model.ArtistJson;

import java.util.concurrent.CompletableFuture;

@Service
public class ArtistGrpcClientService {

    private final ArtistServiceGrpc.ArtistServiceFutureStub artistServiceFutureStub;

    public ArtistGrpcClientService(ArtistServiceGrpc.ArtistServiceFutureStub artistServiceFutureStub) {
        this.artistServiceFutureStub = artistServiceFutureStub;
    }

    // Получить всех художников с пагинацией
    public CompletableFuture<ArtistsPageResponse> getAllArtists(String searchQuery, Pageable pageable) {
        AllArtistRequest request = AllArtistRequest.newBuilder()
                .setSearchQuery(searchQuery==null?"":searchQuery)
                .setPageable(ArtistsPageRequest.newBuilder()
                        .setPage(pageable.getPageNumber())
                        .setSize(pageable.getPageSize())
                        .build())
                .build();
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return artistServiceFutureStub.allArtist(request).get();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to fetch artist: " + e.getMessage(), e);
                    }
                }
        );
    }

    // Получить художника по ID
    public CompletableFuture<ArtistResponse> getArtistById(String id) {
        ArtistsIdRequest request = ArtistsIdRequest.newBuilder()
                .setId(id)
                .build();

        return CompletableFuture.supplyAsync(() -> {
            try {
                return artistServiceFutureStub.artistById(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch artist: " + e.getMessage(), e);
            }
        });
    }

    // Добавить нового художника
    public CompletableFuture<ArtistResponse> addArtist(ArtistJson artist) {
        ArtistRequest request = ArtistRequest.newBuilder()
                .setName(artist.name())
                .setBiography(artist.biography())
                .setPhoto(artist.photo())
                .build();
        return CompletableFuture.supplyAsync(() -> {
            try {
                return artistServiceFutureStub.addArtist(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch artist: " + e.getMessage(), e);
            }
        });
    }

    // Обновить существующего художника
    public CompletableFuture<ArtistResponse> updateArtist(ArtistJson artist) {
        ArtistUpdateRequest request = ArtistUpdateRequest.newBuilder()
                .setId(artist.id().toString())
                .setName(artist.name())
                .setBiography(artist.biography())
                .setPhoto(artist.photo())
                .build();


        return CompletableFuture.supplyAsync(() -> {
            try {
                return artistServiceFutureStub.updateArtist(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch artist: " + e.getMessage(), e);
            }
        });
    }
}
