package rococo.service;

import io.grpc.StatusRuntimeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rococo.controller.exception.ResourceNotFoundException;
import rococo.controller.exception.ValidationException;
import rococo.service.client.CountryGrpcClientService;
import rococo.model.CountryJson;
import rococo.service.exception.GrpcExceptionUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionException;

@Component
public class CountryServiceToClientGrpc implements CountryService {

    private final CountryGrpcClientService countryGrpcClientService;

    public CountryServiceToClientGrpc(CountryGrpcClientService countryGrpcClientService) {
        this.countryGrpcClientService = countryGrpcClientService;
    }


    @Override
    public Page<CountryJson> getAllCountries(String searchQuery, Pageable pageable) {
        try {
            return countryGrpcClientService.getAllCountries(searchQuery, pageable)
                    .thenApply(response -> {
                        List<CountryJson> countries = response.getCountriesList().stream()
                                .map(CountryJson::fromCountryResponse)
                                .toList();

                        return new PageImpl<>(
                                countries,
                                pageable,
                                response.getTotalElements()
                        );
                    })
                    .join();
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e);
        }
    }

    @Override
    public Optional<CountryJson> getCountryById(UUID countryId) {
        try {
            return Optional.of(CountryJson.fromCountryResponse(
                    countryGrpcClientService.getCountryById(countryId.toString()).join()
            ));
        } catch (CompletionException e) {
            throw GrpcExceptionUtil.convertGrpcException(e, countryId);
        }
    }

}
