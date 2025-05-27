package rococo.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface MuseumRepository extends JpaRepository<MuseumEntity, UUID> {
    @Nonnull
    @Query("SELECT m FROM MuseumEntity m WHERE m.title LIKE %:title%")
    Page<MuseumEntity> findByMuseumPage(@Param("title") String searchQuery, @Nonnull Pageable pageable);
}
