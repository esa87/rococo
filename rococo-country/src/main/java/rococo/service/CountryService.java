package rococo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rococo.model.CountryJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface CountryService {
    Page<CountryJson> allCountries(
            @Nullable String searchQuery,
            @Nonnull Pageable pageable
            );

    CountryJson countryById(UUID countryId);
}