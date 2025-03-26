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
import rococo.domain.Artist;
import rococo.model.ArtistJson;
import rococo.service.ArtistService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {
    private final ArtistService artistService;
    private static final Logger log = LoggerFactory.getLogger(ArtistController.class);

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping()
    public ResponseEntity<Page<ArtistJson>> all(
            @RequestParam(required = false) String name,
            @PageableDefault Pageable pageable) {
        try {
            return ResponseEntity.ok(artistService.allArtist(name, pageable));
        } catch (Exception e) {
            log.error("Failed to get artists list", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve artists",
                    e
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistJson> currentArtist(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(artistService.artistById(id));
        } catch (Exception e) {
            log.error("Failed to get artist with id: " + id, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to retrieve artist",
                    e
            );
        }
    }

    @PostMapping()
    public ResponseEntity<ArtistJson> addArtist(@RequestBody ArtistJson artist) {
        try {
            return ResponseEntity.ok(artistService.addArtist(artist));
        } catch (Exception e) {
            log.error("Failed to add artist", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to add artist",
                    e
            );
        }
    }

    @PatchMapping()
    public ResponseEntity<ArtistJson> updateArtist(@RequestBody ArtistJson artist) {
        try {
            return ResponseEntity.ok(artistService.updateArtist(artist));
        } catch (Exception e) {
            log.error("Failed to update artist", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to update artist",
                    e
            );
        }
    }
}
