package rococo.service;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rococo.data.MuseumEntity;
import rococo.data.MuseumRepository;
import rococo.model.MuseumJson;

import java.util.UUID;

@Component
public class MuseumServiceDb implements MuseumService {

    private final MuseumRepository museumRepository;

    @Autowired
    public MuseumServiceDb(MuseumRepository museumRepository) {
        this.museumRepository = museumRepository;
    }

    @Override
    public Page<MuseumJson> allMuseums(@Nullable String searchQuery,
                                       @Nonnull Pageable pageable) {
        Page<MuseumEntity> museumEntity = searchQuery == null
                ? museumRepository.findAll(pageable)
                : museumRepository.findByMuseumPage(searchQuery, pageable);
        return museumEntity.map(MuseumJson::fromMuseumEntity);
    }

    @Override
    public MuseumJson museumById(UUID museumId) {
        return museumRepository.findById(museumId)
                .map(me -> MuseumJson.fromMuseumEntity(me)).orElseThrow(() -> new RuntimeException("Museum not found this id: " + museumId));
    }

    @Override
    public MuseumJson addMuseum(@Nonnull MuseumJson museumJson) {
        MuseumEntity museumEntity = new MuseumEntity().fromMuseumJson(museumJson);
        museumRepository.save(museumEntity);
        return MuseumJson.fromMuseumEntity(museumEntity);
    }

    @Override
    public MuseumJson updateMuseum(@Nonnull MuseumJson museumJson) {
        if (museumRepository.findById(museumJson.id()).isPresent()) {
            MuseumEntity museumEntity = new MuseumEntity().fromMuseumJson(museumJson);
            museumRepository.save(museumEntity);
            return MuseumJson.fromMuseumEntity(museumEntity);
        } else {
            new RuntimeException("Museum not found this id: " + museumJson.id());
            return null;
        }
    }
}
