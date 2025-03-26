package rococo.service;


import grpc.rococo.ArtistResponse;
import grpc.rococo.ArtistsPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rococo.service.client.ArtistGrpcClientService;
import rococo.model.ArtistJson;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class ArtistServiceToClientGrpc implements ArtistService {

    private final ArtistGrpcClientService artistGrpcClientService;

    @Autowired
    public ArtistServiceToClientGrpc(ArtistGrpcClientService artistGrpcClientService) {
        this.artistGrpcClientService = artistGrpcClientService;
    }

    @Override
    public Page<ArtistJson> allArtist(String searchQuery,
                                      Pageable pageable) {
        CompletableFuture<ArtistsPageResponse> response = artistGrpcClientService.getAllArtists(searchQuery, pageable);

        return response.thenApply(resp -> {
            List<ArtistJson> artistJsonList = resp.getArtistsList().stream()
                    .map(ArtistJson::fromArtistResponse)
                    .toList();
            return new PageImpl<>(artistJsonList);
        }).join();
    }

    @Override
    public ArtistJson artistById(UUID artistId) {
        CompletableFuture<ArtistResponse> response = artistGrpcClientService.getArtistById(artistId.toString());
        return response.thenApply(ArtistJson::fromArtistResponse).join();
    }

    @Override
    public ArtistJson addArtist(ArtistJson artistJson) {
        CompletableFuture<ArtistResponse> response = artistGrpcClientService.addArtist(artistJson);

        return response.thenApply(ArtistJson::fromArtistResponse).join();
    }

    @Override
    public ArtistJson updateArtist(ArtistJson artistJson) {
        CompletableFuture<ArtistResponse> response = artistGrpcClientService.updateArtist(artistJson);
        return response.thenApply(ArtistJson::fromArtistResponse).join();
    }
}
