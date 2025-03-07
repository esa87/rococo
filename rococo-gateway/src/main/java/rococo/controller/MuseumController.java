package rococo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import rococo.domain.Museum;
import rococo.model.MuseumJson;
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

    @GetMapping()
    public Page<MuseumJson> all(@RequestParam(required = false) String title,
                                @PageableDefault Pageable pageable) {
        return museumService.allMuseums(title, pageable);
    }

    @GetMapping("/{id}")
    public MuseumJson id(@PathVariable UUID id) {
        return museumService.museumById(id);
    }

    @PostMapping()
    public MuseumJson add(@RequestBody MuseumJson museumJson) {
        return museumService.addMuseum(museumJson);
    }

    @PatchMapping()
    public MuseumJson update(@RequestBody MuseumJson museumJson) {
       return museumService.updateMuseum(museumJson);
    }

}
