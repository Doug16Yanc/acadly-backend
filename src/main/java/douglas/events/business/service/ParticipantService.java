package douglas.events.business.service;

import douglas.events.application.dto.ParticipantDto;
import douglas.events.business.service.dto.ParticipantCreatedEventDTO;
import douglas.events.infraestructure.exception.local.EnrollmentException;
import douglas.events.infraestructure.exception.local.EventNotFoundException;
import douglas.events.infraestructure.exception.local.ListEmptyException;
import douglas.events.infraestructure.exception.local.NotFoundException;
import douglas.events.infraestructure.model.Participant;
import douglas.events.infraestructure.repository.EventRepository;
import douglas.events.infraestructure.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final EnrollmentService enrollmentService;
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public Participant createParticipant(Long eventId, Participant participant) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Evento não encontrado com o ID: " + eventId);
        }

        var savedParticipant = participantRepository.save(participant);

        var enrollment = enrollmentService.createEnrollment(eventId, savedParticipant.getId());

        if (enrollment == null) {
            throw new EnrollmentException("Não foi possível criar a inscrição para o participante.");
        }

        this.eventPublisher.publishEvent(new ParticipantCreatedEventDTO(enrollment));

        return savedParticipant;
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
