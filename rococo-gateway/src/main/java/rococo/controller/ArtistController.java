package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import rococo.domain.Artist;
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

    @GetMapping("/all")
    public List<Artist> all() {
        return artistService.allArtist();
    }

    @GetMapping("/current")
    public Artist currentArtist(@RequestPart UUID artistId) {
        return artistService.artistById(artistId);
    }
}
