package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Page<PaintingJson>> all(
            @RequestParam(required = false) String title,
            @PageableDefault Pageable pageable) {
        try {
            return ResponseEntity.ok(paintingService.allPaintings(title, pageable));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get paintings", e);
        }
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<Page<PaintingJson>> allForArtist(
            @PathVariable UUID id,
            @PageableDefault Pageable pageable) {
        try {
            return ResponseEntity.ok(paintingService.allPaintingsForArtist(id, pageable));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get artist paintings", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaintingJson> currentPainting(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(paintingService.paintingById(id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get painting", e);
        }
    }

    @PostMapping()
    public ResponseEntity<PaintingJson> add(@RequestBody PaintingJson paintingJson) {
        try {
            return ResponseEntity.ok(paintingService.addPainting(paintingJson));
        } catch (Exception e) {
            throw new RuntimeException("Failed to add painting", e);
        }
    }

    @PatchMapping()
    public ResponseEntity<PaintingJson> update(@RequestBody PaintingJson paintingJson) {
        try {
            return ResponseEntity.ok(paintingService.updatePainting(paintingJson));
        } catch (Exception e) {
            throw new RuntimeException("Failed to update painting", e);
        }
    }
}
