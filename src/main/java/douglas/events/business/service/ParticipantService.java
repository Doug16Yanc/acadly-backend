package douglas.events.business.service;

import douglas.events.application.dto.ParticipantDto;
import douglas.events.infraestructure.exception.local.ListEmptyException;
import douglas.events.infraestructure.exception.local.NotFoundException;
import douglas.events.infraestructure.model.Participant;
import douglas.events.infraestructure.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public Participant createParticipant(Long eventId, ParticipantDto participantDto) {
        var existentEvent = participantRepository.findById(eventId);

        var savedParticipant = ParticipantDto.toEntity(participantDto);

        return participantRepository.save(savedParticipant);
    }

    public List<ParticipantDto> getAllParticipants() {
        var participants = participantRepository.findAll();

        if (participants.isEmpty()) {
            throw new ListEmptyException("Não há participantes inscritos.");
        }

        return participants.stream()
                .map(ParticipantDto::fromEntity)
                .toList();
    }

    public ParticipantDto getParticipantById(Long participantId) {
        var participant = participantRepository.findById(participantId);

        if (participant.isEmpty()) {
            throw new NotFoundException("Participante não encontrado.");
        }

        return ParticipantDto.fromEntity(participant.get());
    }

    public Participant getParticipantEntityById(Long participantId) {
        var participant = participantRepository.findById(participantId);

        if (participant.isEmpty()) {
            throw new NotFoundException("Participante não encontrado.");
        }

        return participant.get();
    }
}
