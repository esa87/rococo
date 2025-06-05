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
import rococo.service.exception.GrpcExceptionUtil;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@Component
public class MuseumServiceToClientGrpc implements MuseumService {

    private final MuseumGrpcClientService museumGrpcClientService;
    private final CountryGrpcClientService countryGrpcClientService;

    @Autowired
    public MuseumServiceToClientGrpc(MuseumGrpcClientService museumGrpcClientService,
                                     CountryGrpcClientService countryGrpcClientService) {
        this.museumGrpcClientService = museumGrpcClientService;
        this.countryGrpcClientService = countryGrpcClientService;
    }

    @Override
    public Page<MuseumJson> allMuseums(@Nullable String title,
                                       @Nonnull Pageable pageable) {
        try {
            MuseumsPageResponse response = museumGrpcClientService.getAllMuseums(title, pageable).join();

            List<MuseumJson> museums = response.getMuseumsList().parallelStream()
                    .map(museum -> {
                        try {
                            CountryResponse country = countryGrpcClientService
                                    .getCountryById(museum.getCountryId())
                                    .join();
                            return MuseumJson.fromMuseumResponse(museum, country);
                        } catch (CompletionException e) {
                            throw GrpcExceptionUtil.convertGrpcException(e);
                        }
                    })
                    .collect(Collectors.toList());

            return new PageImpl<>(
                    museums,
                    pageable,
                    response.getTotalElements()
            );
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e);
        }
    }

    @Override
    public MuseumJson museumById(@Nonnull UUID museumId) {
        try {
            MuseumResponse museum = museumGrpcClientService.getMuseumById(museumId.toString()).join();
            CountryResponse country = countryGrpcClientService.getCountryById(museum.getCountryId()).join();
            return MuseumJson.fromMuseumResponse(museum, country);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, museumId);
        }
    }

    @Override
    public MuseumJson addMuseum(@Nonnull MuseumJson museumJson) {
        try {
            MuseumResponse museum = museumGrpcClientService.addMuseum(museumJson).join();
            CountryResponse country = countryGrpcClientService.getCountryById(museum.getCountryId()).join();
            return MuseumJson.fromMuseumResponse(museum, country);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e);
        }
    }

    @Override
    public MuseumJson updateMuseum(@Nonnull MuseumJson museumJson) {
        try {
            MuseumResponse museum = museumGrpcClientService.updateMuseum(museumJson).join();
            CountryResponse country = countryGrpcClientService.getCountryById(museum.getCountryId()).join();
            return MuseumJson.fromMuseumResponse(museum, country);
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, museumJson.id());
        }
    }
}
