package rococo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rococo.model.ArtistJson;

import java.util.UUID;

public interface ArtistService {
    Page<ArtistJson> allArtist(String name,
                               Pageable pageable);

    ArtistJson artistById(UUID artistId);

    ArtistJson addArtist(ArtistJson artistJson);

    ArtistJson updateArtist(ArtistJson artistJson);
}