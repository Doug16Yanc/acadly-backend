package douglas.events.application.controller;

import douglas.events.business.service.EnrollmentService;
import douglas.events.infraestructure.model.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/get-all-by-event/{eventId}")
    public ResponseEntity<List<Enrollment>> getAllByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok().body(enrollmentService.getAllEnrollmentsByEventId(eventId));
    }
}
