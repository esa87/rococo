package rococo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rococo.model.PaintingJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface PaintingService {

    Page<PaintingJson> allPaintings(@Nullable String searchQuery,
                                    @Nonnull Pageable pageable);

    Page<PaintingJson> allPaintingsForArtist(@Nonnull UUID artistId, @Nonnull Pageable pageable);

    PaintingJson paintingById(UUID id);

    PaintingJson addPainting(@Nonnull PaintingJson paintingJson);

    PaintingJson updatePainting(@Nonnull PaintingJson paintingJson);

}