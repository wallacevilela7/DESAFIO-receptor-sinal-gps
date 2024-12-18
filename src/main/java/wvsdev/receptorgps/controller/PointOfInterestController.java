package wvsdev.receptorgps.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wvsdev.receptorgps.dto.CreatePointOfInterest;
import wvsdev.receptorgps.entity.PointOfInterest;
import wvsdev.receptorgps.repository.PointOfInterestRepository;

import java.util.List;

@RestController
public class PointOfInterestController {

    private final PointOfInterestRepository repository;

    public PointOfInterestController(PointOfInterestRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/points-of-interests")
    public ResponseEntity<Void> createPOI(@RequestBody CreatePointOfInterest body) {
        repository.save(new PointOfInterest(body.name(), body.x(), body.y()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/points-of-interests")
    public ResponseEntity<Page<PointOfInterest>> listPOI(@RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        var body = repository.findAll(PageRequest.of(page, pageSize));
        return ResponseEntity.ok(body);
    }

    @GetMapping("/near-me")
    public ResponseEntity<List<PointOfInterest>> nearMe(@RequestParam("x") Long x,
                                                        @RequestParam("y") Long y,
                                                        @RequestParam("dmax") Long dmax) {
        var xMin = x - dmax;
        var xMax = x + dmax;
        var yMin = y - dmax;
        var yMax = y + dmax;

        var body = repository.findNearMe(xMin, xMax, yMin, yMax)
                .stream()
                .filter(p -> distanceBetween(x,y, p.getX(), p.getY()) <= dmax)
                .toList();

        return ResponseEntity.ok(body);
    }

    private Double distanceBetween(Long x1, Long y1, Long x2, Long y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
