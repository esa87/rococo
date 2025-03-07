package rococo.data;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.UUID;

public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {
    @Nonnull
    @Query("SELECT a FROM ArtistEntity a WHERE a.name LIKE %:name%")
    Page<ArtistEntity> findByArtistPage(@Param("name") String searchQuery, @Nonnull Pageable pageable);
}
