package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import rococo.domain.Painting;
import rococo.service.PaintingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/painting")
public class PaintingController {

    private final PaintingService paintingService;

    @Autowired
    public PaintingController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GetMapping("/all")
    public List<Painting> all() {return paintingService.allPaintings();}

    @GetMapping("/current")
    public Painting currentPainting(@RequestPart UUID paintingId) {
        return paintingService.paintingById(paintingId);
    }
}
