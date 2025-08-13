package douglas.events.application.dto;

import douglas.events.infraestructure.model.Participant;
import douglas.events.infraestructure.model.enums.ParticipantType;

public record ParticipantDto(
        String name,
        String email,
        ParticipantType participantType
) {
    public static Participant toEntity(ParticipantDto participantDto) {
        var participant = new Participant();
        participant.setName(participantDto.name());
        participant.setEmail(participantDto.email());
        participant.setParticipantType(participantDto.participantType());
        return participant;
    }

    public static ParticipantDto fromEntity(Participant participant) {
        return new ParticipantDto(
                participant.getName(),
                participant.getEmail(),
                participant.getParticipantType()
        );
    }
}
