package rococo.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rococo.domain.Country;
import rococo.model.CountryJson;

import java.util.List;
import java.util.UUID;

public interface CountryService {
    Page<CountryJson> allCountries(
            @Nullable String searchQuery,
            @Nonnull Pageable pageable
            );

    Country countryById(UUID countryId);
}