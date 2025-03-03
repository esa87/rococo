package rococo.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MuseumRepository extends JpaRepository<MuseumEntity, UUID> {
}
