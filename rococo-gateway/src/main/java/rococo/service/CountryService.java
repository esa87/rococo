package rococo.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rococo.domain.Country;
import rococo.model.CountryJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountryService {
    Page<CountryJson> getAllCountries(
            @Nullable String searchQuery,
            @Nonnull Pageable pageable
            );

    Optional<CountryJson> getCountryById(UUID countryId);
}