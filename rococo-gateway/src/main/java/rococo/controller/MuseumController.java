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
import rococo.domain.Museum;
import rococo.model.MuseumJson;
import rococo.service.MuseumService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/museum")
public class MuseumController {
    private final MuseumService museumService;

    @Autowired
    public MuseumController(MuseumService museumService) {
        this.museumService = museumService;
    }

    @GetMapping
    public Page<MuseumJson> getAllMuseums(
            @RequestParam(required = false) String title,
            @PageableDefault @Nonnull Pageable pageable) {
        return museumService.allMuseums(title, pageable);
    }

    @GetMapping("/{id}")
    public MuseumJson getMuseumById(@PathVariable @Nonnull UUID id) {
        return museumService.museumById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MuseumJson createMuseum(@RequestBody @Nonnull MuseumJson museumJson) {
        return museumService.addMuseum(museumJson);
    }

    @PatchMapping
    public MuseumJson updateMuseum(@RequestBody @Nonnull MuseumJson museumJson) {
        return museumService.updateMuseum(museumJson);
    }
}