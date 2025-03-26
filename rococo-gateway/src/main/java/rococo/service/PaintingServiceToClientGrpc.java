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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class PaintingServiceToClientGrpc implements PaintingService {


    private final PaintingGrpcClientService paintingGrpcClientService;
    private final MuseumGrpcClientService museumGrpcClientService;
    private final ArtistGrpcClientService artistGrpcClientService;
    private final CountryGrpcClientService countryGrpcClientService;

    @Autowired
    public PaintingServiceToClientGrpc(PaintingGrpcClientService paintingGrpcClientService, MuseumGrpcClientService museumGrpcClientService, ArtistGrpcClientService artistGrpcClientService, CountryGrpcClientService countryGrpcClientService) {

        this.paintingGrpcClientService = paintingGrpcClientService;
        this.museumGrpcClientService = museumGrpcClientService;
        this.artistGrpcClientService = artistGrpcClientService;
        this.countryGrpcClientService = countryGrpcClientService;
    }

    @Override
    public Page<PaintingJson> allPaintings(@Nullable String searchQuery, @Nonnull Pageable pageable) {
        // Асинхронный запрос списка картин
        CompletableFuture<PaintingsPageResponse> paintingsFuture = paintingGrpcClientService.getAllPaintings(searchQuery, pageable);

        // Обработка результата
        return paintingsFuture.thenCompose(response -> {
            // Создаем список асинхронных задач для каждой картины
            List<CompletableFuture<PaintingJson>> paintingFutures = response.getPaintingsList().stream()
                    .map(painting -> {
                        // Асинхронный запрос музея
                        CompletableFuture<MuseumResponse> museumFuture = museumGrpcClientService.getMuseumById(painting.getMuseumId());

                        // Асинхронный запрос страны на основе музея
                        CompletableFuture<CountryResponse> countryFuture = museumFuture.thenCompose(museum ->
                                countryGrpcClientService.getCountryById(museum.getCountryId())
                        );

                        // Асинхронный запрос художника
                        CompletableFuture<ArtistResponse> artistFuture = artistGrpcClientService.getArtistById(painting.getArtistId());

                        // Объединение результатов
                        return CompletableFuture.allOf(museumFuture, countryFuture, artistFuture)
                                .thenApply(v -> {
                                    MuseumResponse museum = museumFuture.join();
                                    CountryResponse country = countryFuture.join();
                                    ArtistResponse artist = artistFuture.join();

                                    return PaintingJson.fromPaintingResponse(painting, country, museum, artist);
                                });
                    })
                    .collect(Collectors.toList());

            // Ожидание завершения всех задач
            return CompletableFuture.allOf(paintingFutures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> {
                        List<PaintingJson> paintingJsons = paintingFutures.stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList());

                        return new PageImpl<>(paintingJsons);
                    });
        }).join(); // Блокируем поток и возвращаем результат
    }

    @Override
    public Page<PaintingJson> allPaintingsForArtist(UUID artistId, Pageable pageable) {
        // Асинхронный запрос списка картин для художника
        CompletableFuture<PaintingsPageResponse> paintingsFuture = paintingGrpcClientService.getAllPaintingsForArtist(artistId.toString(), pageable);

        // Обработка результата
        return paintingsFuture.thenCompose(response -> {
            // Создаем список асинхронных задач для каждой картины
            List<CompletableFuture<PaintingJson>> paintingFutures = response.getPaintingsList().stream()
                    .map(painting -> {
                        // Асинхронный запрос музея
                        CompletableFuture<MuseumResponse> museumFuture = museumGrpcClientService.getMuseumById(painting.getMuseumId());

                        // Асинхронный запрос страны на основе музея
                        CompletableFuture<CountryResponse> countryFuture = museumFuture.thenCompose(museum ->
                                countryGrpcClientService.getCountryById(museum.getCountryId())
                        );

                        // Асинхронный запрос художника
                        CompletableFuture<ArtistResponse> artistFuture = artistGrpcClientService.getArtistById(painting.getArtistId());

                        // Объединение результатов
                        return CompletableFuture.allOf(museumFuture, countryFuture, artistFuture)
                                .thenApply(v -> {
                                    MuseumResponse museum = museumFuture.join();
                                    CountryResponse country = countryFuture.join();
                                    ArtistResponse artist = artistFuture.join();

                                    return PaintingJson.fromPaintingResponse(painting, country, museum, artist);
                                });
                    })
                    .collect(Collectors.toList());

            // Ожидание завершения всех задач
            return CompletableFuture.allOf(paintingFutures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> {
                        List<PaintingJson> paintingJsons = paintingFutures.stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList());

                        return new PageImpl<>(paintingJsons);
                    });
        }).join(); // Блокируем поток и возвращаем результат
    }

    @Override
    public PaintingJson paintingById(UUID id) {
        // Асинхронный запрос картины по ID
        CompletableFuture<PaintingResponse> paintingFuture = paintingGrpcClientService.getPaintingById(id.toString());

        // Обработка результата
        return paintingFuture.thenCompose(painting -> {
            // Асинхронный запрос музея
            CompletableFuture<MuseumResponse> museumFuture = museumGrpcClientService.getMuseumById(painting.getMuseumId());

            // Асинхронный запрос страны на основе музея
            CompletableFuture<CountryResponse> countryFuture = museumFuture.thenCompose(museum ->
                    countryGrpcClientService.getCountryById(museum.getCountryId())
            );

            // Асинхронный запрос художника
            CompletableFuture<ArtistResponse> artistFuture = artistGrpcClientService.getArtistById(painting.getArtistId());

            // Объединение результатов
            return CompletableFuture.allOf(museumFuture, countryFuture, artistFuture)
                    .thenApply(v -> {
                        MuseumResponse museum = museumFuture.join();
                        CountryResponse country = countryFuture.join();
                        ArtistResponse artist = artistFuture.join();

                        return PaintingJson.fromPaintingResponse(painting, country, museum, artist);
                    });
        }).exceptionally(ex -> {
            throw new RuntimeException("Painting not found with id: " + id, ex);
        }).join(); // Блокируем поток и возвращаем результат
    }

    @Override
    public PaintingJson addPainting(PaintingJson paintingJson) {
        // Асинхронный запрос художника по ID
        CompletableFuture<ArtistResponse> artistFuture = artistGrpcClientService.getArtistById(paintingJson.artist().id().toString());

        // Асинхронный запрос музея по ID
        CompletableFuture<MuseumResponse> museumFuture = museumGrpcClientService.getMuseumById(paintingJson.museum().id().toString());

        // Объединение результатов
        return artistFuture.thenCombine(museumFuture, (artistResponse, museumResponse) -> {
            // Асинхронный запрос на добавление картины
            return paintingGrpcClientService.addPainting(paintingJson);
        }).thenCompose(addPaintingFuture -> addPaintingFuture.thenApply(paintingResponse -> {
            // Преобразование результата в PaintingJson
            return PaintingJson.fromPaintingResponse(
                    paintingResponse,
                    countryGrpcClientService.getCountryById(museumFuture.join().getCountryId()).join(),
                    museumFuture.join(),
                    artistFuture.join()
            );
        })).exceptionally(ex -> {
            throw new RuntimeException("Failed to add painting: " + ex.getMessage(), ex);
        }).join(); // Блокируем поток и возвращаем результат
    }

    @Override
    public PaintingJson updatePainting(PaintingJson paintingJson) {
        // Асинхронный запрос картины по ID
        CompletableFuture<PaintingResponse> paintingFuture = paintingGrpcClientService.getPaintingById(paintingJson.id().toString());

        // Асинхронный запрос художника по ID
        CompletableFuture<ArtistResponse> artistFuture = artistGrpcClientService.getArtistById(paintingJson.artist().id().toString());

        // Асинхронный запрос музея по ID
        CompletableFuture<MuseumResponse> museumFuture = museumGrpcClientService.getMuseumById(paintingJson.museum().id().toString());

        // Объединение результатов
        return paintingFuture.thenCompose(paintingResponse -> {
            if (paintingResponse != null) {
                return artistFuture.thenCombine(museumFuture, (artistResponse, museumResponse) -> {
                    // Асинхронный запрос на обновление картины
                    return paintingGrpcClientService.updatePainting(paintingJson);
                }).thenCompose(updatePaintingFuture -> updatePaintingFuture.thenApply(updatedPaintingResponse -> {
                    // Преобразование результата в PaintingJson
                    return PaintingJson.fromPaintingResponse(
                            updatedPaintingResponse,
                            countryGrpcClientService.getCountryById(museumFuture.join().getCountryId()).join(),
                            museumFuture.join(),
                            artistFuture.join()
                    );
                }));
            } else {
                throw new RuntimeException("Painting not found with id: " + paintingJson.id());
            }
        }).exceptionally(ex -> {
            throw new RuntimeException("Failed to update painting: " + ex.getMessage(), ex);
        }).join(); // Блокируем поток и возвращаем результат
    }
}
