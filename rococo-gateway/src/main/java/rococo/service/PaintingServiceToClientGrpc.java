package rococo.service;

import grpc.rococo.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rococo.service.client.ArtistGrpcClientService;
import rococo.service.client.CountryGrpcClientService;
import rococo.service.client.MuseumGrpcClientService;
import rococo.service.client.PaintingGrpcClientService;
import rococo.model.PaintingJson;
import rococo.service.exception.GrpcExceptionUtil;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@Component
public class PaintingServiceToClientGrpc implements PaintingService {

    private final PaintingGrpcClientService paintingGrpcClientService;
    private final MuseumGrpcClientService museumGrpcClientService;
    private final ArtistGrpcClientService artistGrpcClientService;
    private final CountryGrpcClientService countryGrpcClientService;

    @Autowired
    public PaintingServiceToClientGrpc(PaintingGrpcClientService paintingGrpcClientService,
                                       MuseumGrpcClientService museumGrpcClientService,
                                       ArtistGrpcClientService artistGrpcClientService,
                                       CountryGrpcClientService countryGrpcClientService) {
        this.paintingGrpcClientService = paintingGrpcClientService;
        this.museumGrpcClientService = museumGrpcClientService;
        this.artistGrpcClientService = artistGrpcClientService;
        this.countryGrpcClientService = countryGrpcClientService;
    }

    @Override
    public Page<PaintingJson> allPaintings(@Nullable String searchQuery, @Nonnull Pageable pageable) {
        try {
            PaintingsPageResponse response = paintingGrpcClientService.getAllPaintings(searchQuery, pageable).join();

            List<PaintingJson> paintings = response.getPaintingsList().parallelStream()
                    .map(painting -> {
                        try {
                            MuseumResponse museum = museumGrpcClientService.getMuseumById(painting.getMuseumId()).join();
                            CountryResponse country = countryGrpcClientService.getCountryById(museum.getCountryId()).join();
                            ArtistResponse artist = artistGrpcClientService.getArtistById(painting.getArtistId()).join();
                            return PaintingJson.fromPaintingResponse(painting, country, museum, artist);
                        } catch (CompletionException e) {
                            throw GrpcExceptionUtil.convertGrpcException(e);
                        }
                    })
                    .collect(Collectors.toList());

            return new PageImpl<>(paintings);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e);
        }
    }

    @Override
    public Page<PaintingJson> allPaintingsForArtist(UUID artistId, Pageable pageable) {
        try {
            PaintingsPageResponse response = paintingGrpcClientService.getAllPaintingsForArtist(artistId.toString(), pageable).join();

            List<PaintingJson> paintings = response.getPaintingsList().parallelStream()
                    .map(painting -> {
                        try {
                            MuseumResponse museum = museumGrpcClientService.getMuseumById(painting.getMuseumId()).join();
                            CountryResponse country = countryGrpcClientService.getCountryById(museum.getCountryId()).join();
                            ArtistResponse artist = artistGrpcClientService.getArtistById(painting.getArtistId()).join();
                            return PaintingJson.fromPaintingResponse(painting, country, museum, artist);
                        } catch (CompletionException e) {
                            throw GrpcExceptionUtil.convertGrpcException(e);
                        }
                    })
                    .collect(Collectors.toList());

            return new PageImpl<>(paintings);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, artistId);
        }
    }

    @Override
    public PaintingJson paintingById(UUID id) {
        try {
            PaintingResponse painting = paintingGrpcClientService.getPaintingById(id.toString()).join();
            MuseumResponse museum = museumGrpcClientService.getMuseumById(painting.getMuseumId()).join();
            CountryResponse country = countryGrpcClientService.getCountryById(museum.getCountryId()).join();
            ArtistResponse artist = artistGrpcClientService.getArtistById(painting.getArtistId()).join();
            return PaintingJson.fromPaintingResponse(painting, country, museum, artist);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, id);
        }
    }

    @Override
    public PaintingJson addPainting(PaintingJson paintingJson) {
        try {
            ArtistResponse artist = artistGrpcClientService.getArtistById(paintingJson.artist().id().toString()).join();
            MuseumResponse museum = museumGrpcClientService.getMuseumById(paintingJson.museum().id().toString()).join();

            PaintingResponse painting = paintingGrpcClientService.addPainting(paintingJson).join();
            CountryResponse country = countryGrpcClientService.getCountryById(museum.getCountryId()).join();

            return PaintingJson.fromPaintingResponse(painting, country, museum, artist);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e);
        }
    }

    @Override
    public PaintingJson updatePainting(PaintingJson paintingJson) {
        try {
            // Проверка существования картины
            paintingGrpcClientService.getPaintingById(paintingJson.id().toString()).join();

            ArtistResponse artist = artistGrpcClientService.getArtistById(paintingJson.artist().id().toString()).join();
            MuseumResponse museum = museumGrpcClientService.getMuseumById(paintingJson.museum().id().toString()).join();

            PaintingResponse painting = paintingGrpcClientService.updatePainting(paintingJson).join();
            CountryResponse country = countryGrpcClientService.getCountryById(museum.getCountryId()).join();

            return PaintingJson.fromPaintingResponse(painting, country, museum, artist);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, paintingJson.id());
        }
    }
}
