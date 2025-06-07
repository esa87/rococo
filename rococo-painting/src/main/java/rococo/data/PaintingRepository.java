package rococo.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import javax.annotation.Nullable;
import java.util.UUID;

public interface PaintingRepository extends JpaRepository<PaintingEntity, UUID> {

    @Query("SELECT p FROM PaintingEntity p WHERE " +
            "(:title IS NULL OR p.title LIKE CONCAT('%', :title, '%'))")
    Page<PaintingEntity> findByPaintingPage(
            @Param("title") @Nullable String title,
            @NonNull Pageable pageable);

    @Query("SELECT p FROM PaintingEntity p WHERE p.artistId = :artistId")
    Page<PaintingEntity> findByPaintingPageForArtist(
            @Param("artistId") @NonNull UUID artistId,
            @NonNull Pageable pageable);
}

