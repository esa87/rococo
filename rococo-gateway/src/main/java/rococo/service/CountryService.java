package rococo.service;

import rococo.domain.Country;

import java.util.List;
import java.util.UUID;

public interface CountryService {
    List<Country> allCountries();

    Country countryById(UUID countryId);
}