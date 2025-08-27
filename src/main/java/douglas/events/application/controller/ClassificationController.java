package douglas.events.application.controller;

import douglas.events.infraestructure.model.enums.ParticipantType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classification")
@RequiredArgsConstructor
public class ClassificationController {

    @GetMapping("/types")
    public ResponseEntity<List<ParticipantType>> getTypes() {
        return ResponseEntity.ok(
                List.of(ParticipantType.values())
        );
    }
}
