package rococo.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface MuseumRepository extends JpaRepository<MuseumEntity, UUID> {
    @Nonnull
    @Query(value = "SELECT m FROM MuseumEntity m WHERE " +
            "(:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))",
            countQuery = "SELECT COUNT(m) FROM MuseumEntity m WHERE " +
                    "(:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<MuseumEntity> findByMuseumPage(
            @Param("title") @Nullable String title,
            @Nonnull Pageable pageable);

}
