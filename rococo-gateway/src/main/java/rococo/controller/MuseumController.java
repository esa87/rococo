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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/museum")
public class MuseumController {
    private final MuseumService museumService;
    private static final Logger log = LoggerFactory.getLogger(MuseumController.class);

    @Autowired
    public MuseumController(MuseumService museumService) {
        this.museumService = museumService;
    }

    @GetMapping()
    public ResponseEntity<Page<MuseumJson>> all(
            @RequestParam(required = false) String title,
            @PageableDefault Pageable pageable) {
        try {
            return ResponseEntity.ok(museumService.allMuseums(title, pageable));
        } catch (Exception e) {
            log.error("Failed to get museums. Title filter: '{}', Pageable: {}", title, pageable, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve museums",
                    e
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MuseumJson> id(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(museumService.museumById(id));
        } catch (Exception e) {
            log.error("Failed to get museum with ID: {}", id, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve museum",
                    e
            );
        }
    }

    @PostMapping()
    public ResponseEntity<MuseumJson> add(@RequestBody MuseumJson museumJson) {
        try {
            return ResponseEntity.ok(museumService.addMuseum(museumJson));
        } catch (Exception e) {
            log.error("Failed to add museum: {}", museumJson, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to add museum",
                    e
            );
        }
    }

    @PatchMapping()
    public ResponseEntity<MuseumJson> update(@RequestBody MuseumJson museumJson) {
        try {
            return ResponseEntity.ok(museumService.updateMuseum(museumJson));
        } catch (Exception e) {
            log.error("Failed to update museum: {}", museumJson, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to update museum",
                    e
            );
        }
    }
}