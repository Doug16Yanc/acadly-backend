package douglas.events.business.service.dto;

import douglas.events.infraestructure.model.Enrollment;

public record ParticipantCreatedEventDTO(
        Enrollment enrollment
) {
}