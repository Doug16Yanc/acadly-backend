package douglas.events.application.dto.response;

import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.model.enums.ParticipantType;

public record ParticipantResponseDTO(
        Long id,
        String name,
        String email,
        ParticipantType participantType
) {
    public static ParticipantResponseDTO fromEntity(Person participant) {
        return new ParticipantResponseDTO(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.getParticipantType()
        );
    }
}
