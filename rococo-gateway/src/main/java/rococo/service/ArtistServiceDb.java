package rococo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import rococo.data.ArtistEntity;
import rococo.data.ArtistRepository;
import rococo.domain.Artist;
import rococo.model.ArtistJson;

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
    public Page<ArtistJson> allArtist(String name,
                                      Pageable pageable) {
        Page<ArtistEntity> artistEntityPage = name == null
                ? artistRepository.findAll(pageable)
                : artistRepository.findByArtistPage(name, pageable);
        return artistEntityPage.map(ArtistJson::fromArtistEntity);
    }

    @Override
    public ArtistJson artistById(UUID artistId) {

        return artistRepository.findById(artistId)
                .map(ae -> new ArtistJson(
                        ae.getId(),
                        ae.getName(),
                        ae.getBiography(),
                        ae.getPhoto().toString()
                )).orElseThrow(() -> new RuntimeException("Artist not fount this id" + artistId));
    }

    @Override
    public ArtistJson addArtist(ArtistJson artistJson) {
        ArtistEntity entity = ArtistEntity.fromArtistJson(artistJson);
        artistRepository.save(entity);
        return ArtistJson.fromArtistEntity(entity);
    }

    @Override
    public ArtistJson updateArtist(ArtistJson artistJson) {
        if(artistRepository.findById(artistJson.id()).isPresent()){
            ArtistEntity entity = ArtistEntity.fromArtistJson(artistJson);
            artistRepository.save(entity);
            return ArtistJson.fromArtistEntity(entity);
        } else {
            new RuntimeException("Artist not found from id: "+artistJson.id());
            return null;
        }
    }
}
