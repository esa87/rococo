package rococo.data;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

    @Nonnull
    @Query("SELECT c FROM CountryEntity c WHERE c.name LIKE %:name%")
    Page<CountryEntity> findByCountriesPage(@Param("name") String searchQuery, @Nonnull Pageable pageable);
}
