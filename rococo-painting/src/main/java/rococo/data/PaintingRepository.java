package rococo.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface PaintingRepository extends JpaRepository<PaintingEntity, UUID> {

    @Nonnull
    @Query("SELECT p FROM PaintingEntity p WHERE p.title LIKE %:title%")
    Page<PaintingEntity> findByPaintingPage(@Param("title") String searchQuery, @Nonnull Pageable pageable);

    @NonNull
    @Query("SELECT p FROM PaintingEntity p WHERE p.artistId = :artist_id")
    Page<PaintingEntity> findByPaintingPageForArtist(@Param("artist_id") UUID artistId, @NonNull Pageable pageable);
}

