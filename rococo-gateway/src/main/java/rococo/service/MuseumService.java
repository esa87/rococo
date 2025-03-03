package rococo.service;

import rococo.domain.Museum;

import java.util.List;
import java.util.UUID;

public interface MuseumService {
    List<Museum> allMuseums();

    Museum museumById(UUID museumId);
}