package rococo.service.client;

import grpc.rococo.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

@Service
public class CountryGrpcClientService {

    private final CountryServiceGrpc.CountryServiceFutureStub countryServiceFutureStub;

    public CountryGrpcClientService(CountryServiceGrpc.CountryServiceFutureStub countryServiceFutureStub) {
        this.countryServiceFutureStub = countryServiceFutureStub;
    }

    public CompletableFuture<CountryResponse> getCountryById(String id) {
        CountryIdRequest request = CountryIdRequest.newBuilder()
                .setId(id)
                .build();

        return CompletableFuture.supplyAsync(() -> {
            try {
                return countryServiceFutureStub.countryById(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch country: " + e.getMessage(), e);
            }
        });

    }

    public CompletableFuture<CountriesResponse> getAllCountries(String searchQuery, @Nonnull Pageable pageable) {
        AllCountriesRequest request = AllCountriesRequest.newBuilder()
                .setSearchQuery(searchQuery==null?"":searchQuery)
                .setPageable(grpc.rococo.Pageable.newBuilder()
                        .setPage(pageable.getPageNumber())
                        .setSize(pageable.getPageSize())
                        .build()
                )
                .build();
        return CompletableFuture.supplyAsync(() -> {
            try {
                return countryServiceFutureStub.allCountries(request).get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch country: " + e.getMessage(), e);
            }
        });


    }
}
