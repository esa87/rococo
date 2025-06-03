package rococo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rococo.model.ArtistJson;
import rococo.service.ArtistService;

import javax.annotation.Nonnull;
import java.util.UUID;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {
    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/{id}")
    public ArtistJson getArtistById(@PathVariable @Nonnull UUID id) {
        return artistService.getArtistById(id).orElseThrow();
    }

    @GetMapping
    public Page<ArtistJson> getAllArtists(
            @RequestParam(required = false) String name,
            @PageableDefault @Nonnull Pageable pageable) {
        return artistService.getAllArtists(name, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistJson createArtist(@RequestBody @Nonnull ArtistJson artist) {
        return artistService.addArtist(artist);
    }

    @PatchMapping("/{id}")
    public ArtistJson updateArtist(
            @PathVariable @Nonnull UUID id,
            @RequestBody @Nonnull ArtistJson artist) {
        return artistService.updateArtist(artist);
    }
}

