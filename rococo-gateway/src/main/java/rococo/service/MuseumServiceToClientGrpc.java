package rococo.service;

import grpc.rococo.CountryResponse;
import grpc.rococo.MuseumResponse;
import grpc.rococo.MuseumsPageResponse;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rococo.service.client.CountryGrpcClientService;
import rococo.service.client.MuseumGrpcClientService;
import rococo.model.MuseumJson;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class MuseumServiceToClientGrpc implements MuseumService {

    private final MuseumGrpcClientService museumGrpcClientService;
    private final CountryGrpcClientService countryGrpcClientService;

    @Autowired
    public MuseumServiceToClientGrpc(MuseumGrpcClientService museumGrpcClientService, CountryGrpcClientService countryGrpcClientService) {
        this.museumGrpcClientService = museumGrpcClientService;
        this.countryGrpcClientService = countryGrpcClientService;
    }

    @Override
    public Page<MuseumJson> allMuseums(@Nullable String searchQuery,
                                       @Nonnull Pageable pageable) {

        // Асинхронный запрос списка музеев
        CompletableFuture<MuseumsPageResponse> museumsFuture = museumGrpcClientService.getAllMuseums(searchQuery, pageable);

        // Обработка результатов
        return museumsFuture.thenApply(response -> {
            List<CompletableFuture<MuseumJson>> museumFutures = response.getMuseumsList().stream()
                    .map(museum -> {
                        // Асинхронный запрос страны для каждого музея
                        CompletableFuture<CountryResponse> countryFuture = countryGrpcClientService.getCountryById(museum.getCountryId());

                        // Объединение результатов
                        return countryFuture.thenApply(country ->
                                MuseumJson.fromMuseumResponse(museum, country)
                        );
                    })
                    .collect(Collectors.toList());

            // Ожидание завершения всех запросов
            List<MuseumJson> museumJsonList = museumFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            return new PageImpl<>(museumJsonList);
        }).join(); // Блокируем, чтобы вернуть результат

    }

    @Override
    public MuseumJson museumById(@Nonnull UUID museumId) {
        CompletableFuture<MuseumResponse> response = museumGrpcClientService.getMuseumById(museumId.toString());
        CompletableFuture<MuseumJson> result = response.thenCompose(museum -> {
            CompletableFuture<CountryResponse> country = countryGrpcClientService.getCountryById(museum.getCountryId());

            return country.thenApply(c ->
                    MuseumJson.fromMuseumResponse(museum, c)
            );
        });

        // Блокируем поток и возвращаем результат
        return result.join();
    }

    @Override
    public MuseumJson addMuseum(@Nonnull MuseumJson museumJson) {
        CompletableFuture<MuseumResponse> response = museumGrpcClientService.addMuseum(museumJson);
        CompletableFuture<MuseumJson> result = response.thenCompose(
                museum -> {
                    CompletableFuture<CountryResponse> countryResponse = countryGrpcClientService.getCountryById(museum.getCountryId());

                    return countryResponse.thenApply(c ->
                            MuseumJson.fromMuseumResponse(museum, c)
                    );
                }
        );
        return result.join();
    }

    @Override
    public MuseumJson updateMuseum(@Nonnull MuseumJson museumJson) {
        CompletableFuture<MuseumResponse> response = museumGrpcClientService.updateMuseum(museumJson);
        CompletableFuture<MuseumJson> result = response.thenCompose(
                museum -> {
                    CompletableFuture<CountryResponse> countryResponse = countryGrpcClientService.getCountryById(museum.getCountryId());

                    return countryResponse.thenApply(c ->
                            MuseumJson.fromMuseumResponse(museum, c)
                    );
                }
        );
        return result.join();
    }
}
