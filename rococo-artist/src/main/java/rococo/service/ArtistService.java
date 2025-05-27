package rococo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rococo.model.ArtistJson;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface ArtistService {
    Page<ArtistJson> allArtist(String name,
                               Pageable pageable);

    ArtistJson artistById(@Nonnull UUID artistId);

    ArtistJson addArtist(@Nonnull ArtistJson artistJson);

    ArtistJson updateArtist(@Nonnull ArtistJson artistJson);
}