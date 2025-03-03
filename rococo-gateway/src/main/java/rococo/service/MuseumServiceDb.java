package rococo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rococo.data.MuseumRepository;
import rococo.domain.Museum;

import java.util.List;
import java.util.UUID;

@Component
public class MuseumServiceDb implements MuseumService {

    private final MuseumRepository museumRepository;

    @Autowired
    public MuseumServiceDb(MuseumRepository museumRepository) {
        this.museumRepository = museumRepository;
    }

    @Override
    public List<Museum> allMuseums() {
        return museumRepository.findAll().stream()
                .map(me -> {
                    return new Museum(
                            me.getId(),
                            me.getTitle(),
                            me.getDescription(),
                            me.getCity(),
                            me.getPhoto(),
                            me.getCountryId().getId()
                    );
                }).toList();
    }

    @Override
    public Museum museumById(UUID museumId) {
        return museumRepository.findById(museumId)
                .map(me -> new Museum(
                        me.getId(),
                        me.getTitle(),
                        me.getDescription(),
                        me.getCity(),
                        me.getPhoto(),
                        me.getCountryId().getId()
                )).orElseThrow(() -> new RuntimeException("Museum not found this id: " + museumId));
    }
}
