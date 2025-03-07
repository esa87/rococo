package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import rococo.model.PaintingJson;
import rococo.service.PaintingService;

import java.util.UUID;

@RestController
@RequestMapping("/api/painting")
public class PaintingController {

    private final PaintingService paintingService;

    @Autowired
    public PaintingController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GetMapping()
    public Page<PaintingJson> all(@RequestParam(required = false) String title,
                                  @PageableDefault Pageable pageable) {
        return paintingService.allPaintings(title, pageable);
    }

    @GetMapping("/artist/{id}")
    public Page<PaintingJson> allForArtist(@PathVariable UUID id,
                                           @PageableDefault Pageable pageable) {
        return paintingService.allPaintingsForArtist(id, pageable);
    }

    @GetMapping("/{id}")
    public PaintingJson currentPainting(@PathVariable UUID id) {
        return paintingService.paintingById(id);
    }

    @PostMapping()
    public PaintingJson add(@RequestBody PaintingJson paintingJson) {
        return paintingService.addPainting(paintingJson);
    }

    @PatchMapping()
    public PaintingJson update(@RequestBody PaintingJson paintingJson) {
        return paintingService.updatePainting(paintingJson);
    }

}
