package douglas.events.application.controller;

import douglas.events.application.dto.ManualValidationRequest;
import douglas.events.application.dto.response.ApiResponse;
import douglas.events.application.dto.response.EnrollmentResponseDTO;
import douglas.events.application.dto.response.ValidationResponseDTO;
import douglas.events.business.service.EnrollmentService;
import douglas.events.business.service.dto.ValidationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static douglas.events.application.util.UtilApi.getApiResponseEntity;

@RestController
@RequestMapping("/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/get-all-by-event/{eventId}")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> getAllByEventId(
            @PathVariable Long eventId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var enrollments = enrollmentService.getAllEnrollmentsByEventId(eventId, page, pageSize);
        return getApiResponseEntity(enrollments, EnrollmentResponseDTO::fromEntity);
    }

    @PostMapping("/validate/token")
    public ResponseEntity<ValidationResponseDTO> validatePresenceByToken(@RequestBody ValidationRequestDTO request) {
        var validationResult = enrollmentService.validateByToken(request.token());
        return ResponseEntity.ok(validationResult);
    }

    @PostMapping("/validate/code")
    public ResponseEntity<ValidationResponseDTO> validatePresenceByNumericCode(@RequestBody ManualValidationRequest request) {
        var validationResult = enrollmentService.validateByNumericCode(request.eventId(), request.numericCode());
        return ResponseEntity.ok(validationResult);
    }
}
