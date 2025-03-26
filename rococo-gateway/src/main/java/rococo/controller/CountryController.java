package rococo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rococo.model.CountryJson;
import rococo.service.CountryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/country")
public class CountryController {
    private final CountryService countryService;
    private static final Logger log = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping()
    public ResponseEntity<Page<CountryJson>> all(
            @RequestParam(required = false) String name,
            @PageableDefault Pageable pageable) {
        try {
            return ResponseEntity.ok(countryService.allCountries(name, pageable));
        } catch (Exception e) {
            log.error("Failed to get countries list. Name filter: {}, Pageable: {}", name, pageable, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve countries",
                    e
            );
        }
    }

    @GetMapping("/current")
    public ResponseEntity<CountryJson> currentCountry(@RequestPart UUID countryId) {
        try {
            return ResponseEntity.ok(countryService.countryById(countryId));
        } catch (Exception e) {
            log.error("Failed to get country with ID: {}", countryId, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve country",
                    e
            );
        }
    }
}