package douglas.events.application.controller;

import douglas.events.business.service.ClassificationService;
import douglas.events.infraestructure.model.Classification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classification")
@RequiredArgsConstructor
public class ClassificationController {
    private final ClassificationService classificationService;

    @PostMapping("/create-classification")
    public ResponseEntity<Classification> createClassification(@RequestBody Classification classification) {
        var newClassification = classificationService.createClassification(classification);

        return ResponseEntity.ok(newClassification);
    }

    @GetMapping("/find-by-type/{type}")
    public ResponseEntity<List<Classification>> findByType(@PathVariable String type) {
        var classification = classificationService.findClassificationByType(type);

        return ResponseEntity.ok(classification);
    }
}
