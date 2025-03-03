package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import rococo.domain.Museum;
import rococo.service.MuseumService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/museum")
public class MuseumController {
    private final MuseumService museumService;

    @Autowired
    public MuseumController(MuseumService museumService) {
        this.museumService = museumService;
    }

    @GetMapping("/all")
    public List<Museum> all() {
        return museumService.allMuseums();
    }

    @GetMapping("/current")
    public Museum currentMuseum(@RequestPart UUID museumId) {
        return museumService.museumById(museumId);
    }

}
