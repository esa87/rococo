package rococo.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rococo.data.CountryEntity;
import rococo.data.CountryRepository;
import rococo.model.CountryJson;

import java.util.UUID;

@Component
public class CountryServiceDb implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceDb(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Page<CountryJson> allCountries(@Nullable String searchQuery,
                                          @Nonnull Pageable pageable) {
        Page<CountryEntity> countryEntityPage = searchQuery == null
        ?countryRepository.findAll(pageable)
        :countryRepository.findByCountriesPage(searchQuery,pageable);
        return countryEntityPage.map(CountryJson::fromCountryEntity);

    }

    @Override
    public CountryJson countryById(UUID countryId) {
        return countryRepository.findById(countryId)
                .map(ce ->
                        new CountryJson(
                                ce.getId(),
                                ce.getName()
                        )).orElseThrow(() -> new RuntimeException("country not found this id: " + countryId));
    }
}
