package rococo.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import rococo.model.MuseumJson;

import java.util.UUID;

public interface MuseumService {
    Page<MuseumJson> allMuseums(@Nullable String searchQuery,
                                @Nonnull @PageableDefault Pageable pageable);

    MuseumJson museumById(UUID museumId);

    MuseumJson addMuseum(@Nonnull MuseumJson museumJson);

    MuseumJson updateMuseum(@Nonnull MuseumJson museumJson);


}