package rococo.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rococo.data.PaintingEntity;
import rococo.data.PaintingRepository;
import rococo.model.PaintingJson;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class PaintingServiceDb implements PaintingService {

    private final PaintingRepository paintingRepository;

    @Autowired
    public PaintingServiceDb(PaintingRepository paintingRepository) {
        this.paintingRepository = paintingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaintingJson> allPaintings(@Nullable String searchQuery, @Nonnull Pageable pageable) {
        Page<PaintingEntity> entity = searchQuery == null
                ? paintingRepository.findAll(pageable)
                : paintingRepository.findByPaintingPage(searchQuery, pageable);
        return entity.map(PaintingJson::fromPaintingEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaintingJson> allPaintingsForArtist(UUID artistId, Pageable pageable) {
        return paintingRepository.findByPaintingPageForArtist(artistId, pageable).map(PaintingJson::fromPaintingEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PaintingJson paintingById(UUID id) {
        return paintingRepository.findById(id)
                .map(pe -> PaintingJson.fromPaintingEntity(pe)
                ).orElseThrow(() -> new RuntimeException("painting not found this id: " + id));
    }

    @Override
    @Transactional
    public PaintingJson addPainting(PaintingJson paintingJson) {
        PaintingEntity entity = PaintingEntity.fromPaintingJson(paintingJson);
        paintingRepository.save(entity);
        return PaintingJson.fromPaintingEntity(entity);
    }

    @Override
    @Transactional
    public PaintingJson updatePainting(PaintingJson paintingJson) {
        paintingRepository.findById(paintingJson.id())
                .orElseThrow(() -> new EntityNotFoundException("Painting not found from id: " + paintingJson.id()));
        PaintingEntity entity = PaintingEntity.fromPaintingJson(paintingJson);
        new PaintingEntity();
        paintingRepository.save(entity);
        return PaintingJson.fromPaintingEntity(entity);
    }
}
