package douglas.events.business.service;

import douglas.events.infraestructure.exception.local.ListEmptyException;
import douglas.events.infraestructure.model.Certificate;
import douglas.events.infraestructure.model.Enrollment;
import douglas.events.infraestructure.model.enums.EnrollmentStatus;
import douglas.events.infraestructure.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final EventService eventService;
    private final ParticipantService participantService;

    public Enrollment createEnrollment(Long eventId, Long participantId) {
        var existentEvent = eventService.getEventEntityById(eventId);
        var existentParticipant = participantService.getParticipantEntityById(participantId);

        var savedEnrollment = new Enrollment(
                null,
                existentEvent,
                existentParticipant,
                LocalDateTime.now(),
                EnrollmentStatus.CONFIRMADA,
                new Certificate()
        );

        return enrollmentRepository.save(savedEnrollment);
    }

    public List<Enrollment> getAllEnrollmentsByEventId(Long eventId) {
        var enrollments = enrollmentRepository.findEnrollmentsByEventId(eventId);

        if (enrollments.isEmpty()) {
            throw new ListEmptyException("Não há inscrições ainda.");
        }

        return enrollments;
    }
}
