package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import rococo.domain.Country;
import rococo.model.CountryJson;
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

    @GetMapping()
    public Page<CountryJson> all(@RequestParam(required = false) String name,
                                 @PageableDefault Pageable pageable) {
        return countryService.allCountries(name, pageable);
    }

    @GetMapping("/current")
    public Country currentCountry(@RequestPart UUID countryId) {
        return countryService.countryById(countryId);
    }
}
