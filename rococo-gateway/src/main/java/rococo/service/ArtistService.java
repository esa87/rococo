package rococo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rococo.model.ArtistJson;

import java.util.Optional;
import java.util.UUID;

public interface ArtistService {
    Page<ArtistJson> getAllArtists(String searchQuery,
                                   Pageable pageable);

    Optional<ArtistJson> getArtistById(UUID artistId);

    ArtistJson addArtist(ArtistJson artistJson);

    ArtistJson updateArtist(ArtistJson artistJson);
}