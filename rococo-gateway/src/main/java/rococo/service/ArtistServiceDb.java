package rococo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rococo.data.ArtistRepository;
import rococo.domain.Artist;

import java.util.List;
import java.util.UUID;

@Component
public class ArtistServiceDb implements ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistServiceDb(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> allArtist() {
        return artistRepository.findAll()
                .stream()
                .map(ae -> {
                    return new Artist(
                            ae.getId(),
                            ae.getName(),
                            ae.getBiography(),
                            ae.getPhoto()
                    );
                }).toList();

    }

    @Override
    public Artist artistById(UUID artistId) {

        return artistRepository.findById(artistId)
                .map(ae -> new Artist(
                        ae.getId(),
                        ae.getName(),
                        ae.getBiography(),
                        ae.getPhoto()
                )).orElseThrow(() -> new RuntimeException("Artist not fount this id" + artistId));
    }
}
