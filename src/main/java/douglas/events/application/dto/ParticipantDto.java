package douglas.events.application.dto;

import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.model.enums.ParticipantType;

public record ParticipantDto(
        String name,
        String email,
        ParticipantType participantType
) {
    public static Person toEntity(ParticipantDto participantDto) {
        var participant = new Person();
        participant.setName(participantDto.name());
        participant.setEmail(participantDto.email());
        participant.setParticipantType(participantDto.participantType());
        return participant;
    }

    public static ParticipantDto fromEntity(Person participant) {
        return new ParticipantDto(
                participant.getName(),
                participant.getEmail(),
                participant.getParticipantType()
        );
    }
}
