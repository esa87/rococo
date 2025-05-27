package rococo.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {

    @Query(value = "SELECT a FROM ArtistEntity a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name,'%'))",
            countQuery = "SELECT COUNT(a) FROM ArtistEntity a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name,'%'))")
    Page<ArtistEntity> findByArtistPage(@Param("name") String name, Pageable pageable);

}
