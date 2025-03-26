package rococo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;
import rococo.domain.Artist;
import rococo.model.ArtistJson;

import java.util.List;
import java.util.UUID;

public interface ArtistService {
    Page<ArtistJson> allArtist(String searchQuery,
                               Pageable pageable);

    ArtistJson artistById(UUID artistId);

    ArtistJson addArtist(ArtistJson artistJson);

    ArtistJson updateArtist(ArtistJson artistJson);
}