package douglas.events.application.dto;

import douglas.events.infraestructure.model.Participant;
import douglas.events.infraestructure.model.enums.ParticipantType;

import java.util.ArrayList;

public record ParticipantDto(
        String name,
        String email,
        ParticipantType participantType
) {
    public static Participant toEntity(ParticipantDto participantDto) {
        return new Participant(
                null,
                participantDto.name,
                participantDto.email,
                participantDto.participantType,
                new ArrayList<>()
        );
    }

    public static ParticipantDto fromEntity(Participant participant) {
        return new ParticipantDto(
                participant.getName(),
                participant.getEmail(),
                participant.getParticipantType()
        );
    }
}
