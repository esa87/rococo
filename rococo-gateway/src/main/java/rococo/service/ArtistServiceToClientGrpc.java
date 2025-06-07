package rococo.service;


import grpc.rococo.ArtistResponse;
import grpc.rococo.ArtistsPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rococo.controller.exception.*;
import rococo.service.client.ArtistGrpcClientService;
import rococo.model.ArtistJson;
import rococo.service.exception.GrpcExceptionUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionException;

@Component
public class ArtistServiceToClientGrpc implements ArtistService {

    private final ArtistGrpcClientService artistGrpcClientService;

    @Autowired
    public ArtistServiceToClientGrpc(ArtistGrpcClientService artistGrpcClientService) {
        this.artistGrpcClientService = artistGrpcClientService;
    }

    @Override
    public Optional<ArtistJson> getArtistById(UUID id) {
        try {
            ArtistResponse response = artistGrpcClientService.getArtistById(id.toString()).join();
            return Optional.of(ArtistJson.fromArtistResponse(response));
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, id);
        }
    }

    @Override
    public Page<ArtistJson> getAllArtists(String name, Pageable pageable) {
        try {
            return artistGrpcClientService.getAllArtists(name, pageable)
                    .thenApply(response -> {
                        List<ArtistJson> artists = response.getArtistsList().stream()
                                .map(ArtistJson::fromArtistResponse)
                                .toList();

                        return new PageImpl<>(
                                artists,
                                pageable,
                                response.getTotalElements()
                        );
                    })
                    .join();

        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, null);
        }
    }

    @Override
    public ArtistJson addArtist(ArtistJson artist) {
        try {
            ArtistResponse response = artistGrpcClientService.addArtist(artist).join();
            return ArtistJson.fromArtistResponse(response);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, null);
        }
    }

    @Override
    public ArtistJson updateArtist(ArtistJson artist) {
        try {
            ArtistResponse response = artistGrpcClientService.updateArtist(artist).join();
            return ArtistJson.fromArtistResponse(response);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, artist.id());
        }
    }


}
