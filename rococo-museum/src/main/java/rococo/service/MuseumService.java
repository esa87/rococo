package rococo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rococo.model.MuseumJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface MuseumService {
    Page<MuseumJson> allMuseums(@Nullable String searchQuery,
                                @Nonnull Pageable pageable);

    MuseumJson museumById(UUID museumId);

    MuseumJson addMuseum(@Nonnull MuseumJson museumJson);

    MuseumJson updateMuseum(@Nonnull MuseumJson museumJson);


}