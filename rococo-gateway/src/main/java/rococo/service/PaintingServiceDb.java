package rococo.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rococo.data.*;
import rococo.model.PaintingJson;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class PaintingServiceDb implements PaintingService {

    private final PaintingRepository paintingRepository;
    private final ArtistRepository artistRepository;
    private final MuseumRepository museumRepository;

    @Autowired
    public PaintingServiceDb(PaintingRepository paintingRepository, ArtistRepository artistRepository, MuseumRepository museumRepository) {
        this.paintingRepository = paintingRepository;
        this.artistRepository = artistRepository;
        this.museumRepository = museumRepository;
    }

    @Override
    public Page<PaintingJson> allPaintings(@Nullable String searchQuery, @Nonnull Pageable pageable) {
        Page<PaintingEntity> entity = searchQuery == null
                ? paintingRepository.findAll(pageable)
                : paintingRepository.findByPaintingPage(searchQuery, pageable);
        return entity.map(PaintingJson::fromPaintingEntity);
    }

    @Override
    public Page<PaintingJson> allPaintingsForArtist(UUID artistId, Pageable pageable) {
        return paintingRepository.findByPaintingPageForArtist(artistId, pageable).map(PaintingJson::fromPaintingEntity);
    }

    @Override
    public PaintingJson paintingById(UUID id) {
        return paintingRepository.findById(id)
                .map(pe -> PaintingJson.fromPaintingEntity(pe)
                ).orElseThrow(() -> new RuntimeException("painting not found this id: " + id));
    }

    @Override
    public PaintingJson addPainting(PaintingJson paintingJson) {
        PaintingEntity entity = new PaintingEntity();
        entity.setTitle(paintingJson.title());
        entity.setDescription(paintingJson.description());
        ArtistEntity artistEntity = artistRepository.findById(paintingJson.artist().id())
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        MuseumEntity museumEntity = museumRepository.findById(paintingJson.museum().id())
                .orElseThrow(() -> new RuntimeException("Museum not found"));

        entity.setArtistEntity(artistEntity);
        entity.setMuseumEntity(museumEntity);

        if (paintingJson.content() != null) {
            entity.setContent(paintingJson.content().getBytes(StandardCharsets.UTF_8));
        } else {
            entity.setContent(new byte[0]);
        }
        paintingRepository.save(entity);
        return PaintingJson.fromPaintingEntity(entity);
    }

    @Override
    public PaintingJson updatePainting(PaintingJson paintingJson) {
        if (paintingRepository.findById(paintingJson.id()).isPresent()) {
            PaintingEntity entity = new PaintingEntity();
            entity.setTitle(paintingJson.title());
            entity.setDescription(paintingJson.description());
            ArtistEntity artistEntity = artistRepository.findById(paintingJson.artist().id())
                    .orElseThrow(() -> new RuntimeException("Artist not found"));
            MuseumEntity museumEntity = museumRepository.findById(paintingJson.museum().id())
                    .orElseThrow(() -> new RuntimeException("Museum not found"));

            entity.setArtistEntity(artistEntity);
            entity.setMuseumEntity(museumEntity);

            if (paintingJson.content() != null) {
                entity.setContent(paintingJson.content().getBytes(StandardCharsets.UTF_8));
            } else {
                entity.setContent(new byte[0]);
            }
            paintingRepository.save(entity);
            return PaintingJson.fromPaintingEntity(entity);
        } else {
            new RuntimeException("Painting not found from id: " + paintingJson.id());
            return null;
        }
    }

}
