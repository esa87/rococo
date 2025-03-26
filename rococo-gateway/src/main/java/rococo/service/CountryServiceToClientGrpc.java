package rococo.service;

import grpc.rococo.CountriesResponse;
import grpc.rococo.CountryResponse;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rococo.service.client.CountryGrpcClientService;
import rococo.model.CountryJson;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class CountryServiceToClientGrpc implements CountryService {

    private final CountryGrpcClientService countryGrpcClientService;

    @Autowired
    public CountryServiceToClientGrpc(CountryGrpcClientService countryRepository) {
        this.countryGrpcClientService = countryRepository;
    }


    @Override
    public Page<CountryJson> allCountries(@Nullable String searchQuery,
                                          @Nonnull Pageable pageable) {
        CompletableFuture<CountriesResponse> response = countryGrpcClientService.getAllCountries(searchQuery, pageable);
        return response.thenApply(resp -> {
            List<CountryJson> countryJsonList = resp.getCountriesList().stream()
                    .map(CountryJson::fromCountryResponse)
                    .collect(Collectors.toList());
            return new PageImpl<>(countryJsonList);
        }).join();
    }

    @Override
    public CountryJson countryById(UUID countryId) {
        CompletableFuture<CountryResponse> response = countryGrpcClientService.getCountryById(countryId.toString());
        return response.thenApply(CountryJson::fromCountryResponse).join();
    }
}
