package rococo.service;


import jakarta.persistence.EntityNotFoundException;
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
public class ArtistServiceDb implements ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistServiceDb(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
        artistRepository.findById(artistJson.id())
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id" + artistJson.id()));
        ArtistEntity entity = ArtistEntity.fromArtistJson(artistJson);
        artistRepository.save(entity);
        return ArtistJson.fromArtistEntity(entity);
    }
}
