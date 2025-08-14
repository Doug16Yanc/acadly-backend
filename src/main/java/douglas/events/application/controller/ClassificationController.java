package douglas.events.application.controller;

import douglas.events.business.service.ClassificationService;
import douglas.events.infraestructure.model.Classification;
import douglas.events.infraestructure.model.enums.ParticipantType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classification")
@RequiredArgsConstructor
public class ClassificationController {
    private final ClassificationService classificationService;

    @GetMapping("/find-by-type/{type}")
    public ResponseEntity<List<Classification>> findByType(@PathVariable String type) {
        var classification = classificationService.findClassificationByType(type);

        return ResponseEntity.ok(classification);
    }

    @GetMapping("/types")
    public ResponseEntity<List<ParticipantType>> getTypes() {
        return ResponseEntity.ok(
                List.of(ParticipantType.values())
        );
    }
}
