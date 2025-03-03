package rococo.service;

import rococo.domain.Painting;

import java.util.List;
import java.util.UUID;

public interface PaintingService {
    List<Painting> allPaintings();

    Painting paintingById(UUID paintingId);
}