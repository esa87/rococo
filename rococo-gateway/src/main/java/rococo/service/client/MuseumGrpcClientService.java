package rococo.service.client;

import grpc.rococo.*;
import io.grpc.StatusRuntimeException;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import rococo.model.MuseumJson;

import java.util.concurrent.CompletableFuture;

@Service
public class MuseumGrpcClientService {

    private final MuseumServiceGrpc.MuseumServiceFutureStub museumServiceFutureStub;

    public MuseumGrpcClientService(MuseumServiceGrpc.MuseumServiceFutureStub museumServiceFutureStub) {
        this.museumServiceFutureStub = museumServiceFutureStub;
    }


    // Получить все музеи
    @Retryable(
            value = {StatusRuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 1.5)
    )
    public CompletableFuture<MuseumsPageResponse> getAllMuseums(String searchQuery, Pageable pageable) {
        AllMuseumsRequest request = AllMuseumsRequest.newBuilder()
                .setSearchQuery(searchQuery==null?"":searchQuery)
                .setPageable(MuseumsPageRequest.newBuilder()
                        .setPage(pageable.getPageNumber())
                        .setSize(pageable.getPageSize())
                        .build())
                .build();
        return CompletableFuture.supplyAsync(() -> {
            try {
                return museumServiceFutureStub.allMuseums(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch museums: " + e.getMessage(), e);
            }
        });
    }

    //
    // Получить музей по ID
    @Retryable(
            value = {StatusRuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 1.5)
    )
    public CompletableFuture<MuseumResponse> getMuseumById(String id) {
        MuseumsIdRequest request = MuseumsIdRequest.newBuilder()
                .setId(id)
                .build();
        return CompletableFuture.supplyAsync(() -> {
            try {
                return museumServiceFutureStub.museumById(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch museums: " + e.getMessage(), e);
            }
        });

    }

    // Добавить музей
    @Retryable(
            value = {StatusRuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 1.5)
    )
    public CompletableFuture<MuseumResponse> addMuseum(MuseumJson museum) {
        MuseumRequest request = MuseumRequest.newBuilder()
                .setTitle(museum.title())
                .setDescription(museum.description())
                .setCity(museum.geo().city())
                .setPhoto(museum.photo())
                .setCountryId(museum.geo().country().id().toString())
                .build();
        return CompletableFuture.supplyAsync(() -> {
            try {
                return museumServiceFutureStub.addMuseum(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch museums: " + e.getMessage(), e);
            }
        });
    }

    // Обновить музей
    @Retryable(
            value = {StatusRuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 1.5)
    )
    public CompletableFuture<MuseumResponse> updateMuseum(MuseumJson museum) {
        MuseumUpdateRequest request = MuseumUpdateRequest.newBuilder()
                .setId(museum.id().toString())
                .setTitle(museum.title())
                .setDescription(museum.description())
                .setCity(museum.geo().city())
                .setPhoto(museum.photo())
                .setCountryId(museum.geo().country().id().toString())
                .build();
        return CompletableFuture.supplyAsync(() -> {
            try {
                return museumServiceFutureStub.updateMuseum(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch museums: " + e.getMessage(), e);
            }
        });
    }
}
