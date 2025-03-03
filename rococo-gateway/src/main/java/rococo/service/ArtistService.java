package rococo.service;

import rococo.domain.Artist;

import java.util.List;
import java.util.UUID;

public interface ArtistService {
    List<Artist> allArtist();

    Artist artistById(UUID artistId);
}