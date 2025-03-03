package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import rococo.domain.Country;
import rococo.service.CountryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/country")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public List<Country> all() {
        return countryService.allCountries();
    }

    @GetMapping("/current")
    public Country currentCountry(@RequestPart UUID countryId) {
        return countryService.countryById(countryId);
    }
}
