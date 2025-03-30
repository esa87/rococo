package rococo.service;

import rococo.model.MuseumJson;
import rococo.model.PaintingJson;

public interface PaintingClient {
    PaintingJson addPainting (PaintingJson paintingJson);
}
