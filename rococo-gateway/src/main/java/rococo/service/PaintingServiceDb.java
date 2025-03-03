package rococo.service;

import org.springframework.stereotype.Component;
import rococo.data.PaintingRepository;
import rococo.domain.Painting;

import java.util.List;
import java.util.UUID;

@Component
public class PaintingServiceDb implements PaintingService {

    private final PaintingRepository paintingRepository;

    public PaintingServiceDb(PaintingRepository paintingRepository) {
        this.paintingRepository = paintingRepository;
    }

    @Override
    public List<Painting> allPaintings() {
        return paintingRepository.findAll().stream()
                .map(pe -> {
                    return new Painting(
                            pe.getId(),
                            pe.getTitle(),
                            pe.getDescription(),
                            pe.getArtistEntity().stream().map(ae -> {
                                return ae.getId();
                            }).toList(),
                            pe.getMuseumEntity().getId(),
                            pe.getContent()
                    );
                }).toList();
    }

    @Override
    public Painting paintingById(UUID paintingId) {
        return paintingRepository.findById(paintingId)
                .map(pe -> new Painting(
                        pe.getId(),
                        pe.getTitle(),
                        pe.getDescription(),
                        pe.getArtistEntity().stream().map(ae -> {
                            return ae.getId();
                        }).toList(),
                        pe.getMuseumEntity().getId(),
                        pe.getContent()
                )).orElseThrow(() -> new RuntimeException("painting not found this id: " + paintingId));
    }
}
