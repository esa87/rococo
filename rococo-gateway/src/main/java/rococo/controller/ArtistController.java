package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import rococo.domain.Artist;
import rococo.model.ArtistJson;
import rococo.service.ArtistService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {
    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping()
    public Page<ArtistJson> all(@RequestParam(required = false) String name,
                                @PageableDefault Pageable pageable) {
        return artistService.allArtist(name, pageable);
    }

    @GetMapping("/{id}")
    public ArtistJson currentArtist(@PathVariable UUID id) {
        return artistService.artistById(id);
    }

    @PostMapping()
    public ArtistJson addArtist(@RequestBody ArtistJson artist) {
        return artistService.addArtist(artist);
    }

    @PatchMapping()
    public ArtistJson updateArtist(@RequestBody ArtistJson artist) {
        return artistService.updateArtist(artist);
    }
}
