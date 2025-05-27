package rococo.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import rococo.data.CountryEntity;
import rococo.data.CountryRepository;
import rococo.model.CountryJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

@Service
public class CountryServiceDb implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceDb(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountryJson> allCountries(@Nullable String searchQuery,
                                          @Nonnull Pageable pageable) {
        Page<CountryEntity> countryEntityPage = !StringUtils.hasText(searchQuery)
        ?countryRepository.findAll(pageable)
        :countryRepository.findByCountriesPage(searchQuery,pageable);
        return countryEntityPage.map(CountryJson::fromCountryEntity);

    }

    @Override
    @Transactional(readOnly = true)
    public CountryJson countryById(@Nonnull UUID countryId) {
        return countryRepository.findById(countryId)
                .map(ce ->
                        new CountryJson(
                                ce.getId(),
                                ce.getName()
                        )).orElseThrow(() -> new EntityNotFoundException("country not found this id: " + countryId));
    }
}
