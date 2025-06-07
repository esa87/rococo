package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rococo.model.PaintingJson;
import rococo.service.PaintingService;

import javax.annotation.Nonnull;
import java.util.UUID;


@RestController
@RequestMapping("/api/painting")
public class PaintingController {

    private final PaintingService paintingService;

    public PaintingController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GetMapping
    public Page<PaintingJson> getAllPaintings(
            @RequestParam(required = false) String title,
            @PageableDefault @Nonnull Pageable pageable) {
        return paintingService.allPaintings(title, pageable);
    }

    @GetMapping("/author/{id}")
    public Page<PaintingJson> getPaintingsByArtist(
            @PathVariable @Nonnull UUID id,
            @PageableDefault @Nonnull Pageable pageable) {
        return paintingService.allPaintingsForArtist(id, pageable);
    }

    @GetMapping("/{id}")
    public PaintingJson getPaintingById(@PathVariable @Nonnull UUID id) {
        return paintingService.paintingById(id);
    }

    @PostMapping
    public PaintingJson createPainting(@RequestBody @Nonnull PaintingJson paintingJson) {
        return paintingService.addPainting(paintingJson);
    }

    @PatchMapping
    public PaintingJson updatePainting(@RequestBody @Nonnull PaintingJson paintingJson) {
        return paintingService.updatePainting(paintingJson);
    }
}
