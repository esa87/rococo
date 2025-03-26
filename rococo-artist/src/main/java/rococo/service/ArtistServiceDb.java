package rococo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import rococo.data.ArtistEntity;
import rococo.data.ArtistRepository;
import rococo.model.ArtistJson;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ArtistServiceDb implements ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistServiceDb(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Page<ArtistJson> allArtist(String name, Pageable pageable) {
        if (!StringUtils.hasText(name)) {
            return artistRepository.findAll(pageable)
                    .map(ArtistJson::fromArtistEntity);
        } else {
            return artistRepository.findByArtistPage(name, pageable)
                    .map(ArtistJson::fromArtistEntity);
        }

    }

    @Override
    public ArtistJson artistById(UUID artistId) {
        return artistRepository.findById(artistId)
                .map(ae -> ArtistJson.fromArtistEntity(ae))
                .orElseThrow(() -> new RuntimeException("Artist not fount this id" + artistId));
    }

    @Override
    @Transactional
    public ArtistJson addArtist(ArtistJson artistJson) {
        ArtistEntity entity = ArtistEntity.fromArtistJson(artistJson);
        artistRepository.save(entity);
        return ArtistJson.fromArtistEntity(entity);
    }

    @Override
    @Transactional
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
