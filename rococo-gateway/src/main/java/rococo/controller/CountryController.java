package rococo.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rococo.controller.exception.ResourceNotFoundException;
import rococo.model.CountryJson;
import rococo.service.CountryService;

import javax.annotation.Nonnull;
import java.util.UUID;

@RestController
@RequestMapping("/api/country")
public class CountryController {
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<Page<CountryJson>> getAllCountries(
            @RequestParam(required = false) String name,
            @PageableDefault @Nonnull Pageable pageable) {
        return ResponseEntity.ok(countryService.getAllCountries(name, pageable));
    }

    @GetMapping("/current")
    public ResponseEntity<CountryJson> getCountryById(
            @RequestParam @NotNull UUID countryId) {
        CountryJson country = countryService.getCountryById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(countryId));
        return ResponseEntity.ok(country);
    }
}