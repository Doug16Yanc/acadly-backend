package douglas.events.business.service;

import douglas.events.application.dto.ParticipantDto;
import douglas.events.business.service.dto.ParticipantCreatedEventDTO;
import douglas.events.infraestructure.exception.local.EnrollmentException;
import douglas.events.infraestructure.exception.local.EventNotFoundException;
import douglas.events.infraestructure.exception.local.ListEmptyException;
import douglas.events.infraestructure.exception.local.NotFoundException;
import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.model.enums.Role;
import douglas.events.infraestructure.repository.EventRepository;
import douglas.events.infraestructure.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final EnrollmentService enrollmentService;
    private final PersonRepository personRepository;
    private final EventRepository eventRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public Person createParticipant(Long eventId, Person participant) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Evento não encontrado com o ID: " + eventId);
        }
        participant.setRole(Role.PARTICIPANT);
        var savedParticipant = personRepository.save(participant);
        var enrollment = enrollmentService.createEnrollment(eventId, savedParticipant.getId());

        if (enrollment == null) {
            throw new EnrollmentException("Não foi possível criar a inscrição para o participante.");
        }
        this.eventPublisher.publishEvent(new ParticipantCreatedEventDTO(enrollment));
        return savedParticipant;
    }

    public Page<Person> getAllParticipants(Integer page, Integer pageSize) {
        var participants = personRepository.findByRole(Role.PARTICIPANT, PageRequest.of(page, pageSize));

        if (participants.isEmpty()) {
            throw new ListEmptyException("Não há participantes inscritos.");
        }

        return participants;
    }

    public Person getParticipantByEmail(String email) {
        var participant = personRepository.findByEmail(email);

        if (participant == null) {
            throw new NotFoundException("Participante não encontrado.");
        }

        return participant;
    }

    public ParticipantDto getParticipantById(Long participantId) {
        var participant = personRepository.findById(participantId);

        if (participant.isEmpty()) {
            throw new NotFoundException("Participante não encontrado.");
        }

        return ParticipantDto.fromEntity(participant.get());
    }
}
