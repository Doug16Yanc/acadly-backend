package douglas.events.application.dto.response;

import douglas.events.infraestructure.model.Enrollment;
import douglas.events.infraestructure.model.enums.EnrollmentStatus;
import douglas.events.infraestructure.model.enums.WasPresent;

import java.time.LocalDateTime;

public record EnrollmentResponseDTO(
        EventResponseDTO event,
        ParticipantResponseDTO participant,
        LocalDateTime enrollmentDate,
        EnrollmentStatus status,
        WasPresent wasPresent
) {
    public static EnrollmentResponseDTO fromEntity(Enrollment enrollment) {
        return new EnrollmentResponseDTO(
                EventResponseDTO.fromEntity(enrollment.getEvent()),
                ParticipantResponseDTO.fromEntity(enrollment.getParticipant()),
                enrollment.getEnrollmentDate(),
                enrollment.getStatus(),
                enrollment.getWasPresent()
        );
    }
}
