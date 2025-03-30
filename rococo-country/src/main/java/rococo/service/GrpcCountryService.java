package rococo.service;

import grpc.rococo.*;
import io.grpc.stub.StreamObserver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rococo.model.CountryJson;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GrpcCountryService extends CountryServiceGrpc.CountryServiceImplBase {

    private final CountryService countryService;

    public GrpcCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public void allCountries(AllCountriesRequest request, StreamObserver<CountriesResponse> responseObserver) {

        Pageable pageable = PageRequest.of(request.getPageable().getPage(), request.getPageable().getSize());
        Page<CountryJson> countriesPage = countryService.allCountries(request.getSearchQuery(), pageable);

        CountriesResponse response = CountriesResponse.newBuilder()
                .addAllCountries(
                        countriesPage.getContent().stream()
                                .map(country -> CountryResponse.newBuilder()
                                        .setId(country.id().toString())
                                        .setName(country.name())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void countryById(CountryIdRequest request, StreamObserver<CountryResponse> responseObserver) {
        CountryJson countryJson= countryService.countryById(UUID.fromString(request.getId()));

        responseObserver.onNext(
                CountryResponse.newBuilder()
                        .setId(countryJson.id().toString())
                        .setName(countryJson.name())
                        .build()
        );
        responseObserver.onCompleted();
    }
}
