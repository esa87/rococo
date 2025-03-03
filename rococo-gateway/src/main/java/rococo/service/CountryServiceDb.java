package rococo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rococo.data.CountryRepository;
import rococo.domain.Country;

import java.util.List;
import java.util.UUID;

@Component
public class CountryServiceDb implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceDb(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> allCountries() {
        return countryRepository
                .findAll()
                .stream()
                .map(ce -> {
                    return new Country(
                            ce.getId(),
                            ce.getName()
                    );
                }).toList();
    }

    @Override
    public Country countryById(UUID countryId) {
        return countryRepository.findById(countryId)
                .map(ce ->
                        new Country(
                                ce.getId(),
                                ce.getName()
                        )).orElseThrow(() -> new RuntimeException("country not found this id: " + countryId));
    }
}
