package douglas.events.business.service;

import douglas.events.application.dto.response.ValidationResponseDTO;
import douglas.events.infraestructure.exception.local.ListEmptyException;
import douglas.events.infraestructure.exception.local.ParticipantNotFoundException;
import douglas.events.infraestructure.model.Enrollment;
import douglas.events.infraestructure.model.enums.EnrollmentStatus;
import douglas.events.infraestructure.model.enums.WasPresent;
import douglas.events.infraestructure.repository.EnrollmentRepository;
import douglas.events.infraestructure.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final EventService eventService;
    private final PersonRepository personRepository;

    public Enrollment createEnrollment(Long eventId, Long participantId) {
        var existentEvent = eventService.getEventEntityById(eventId);
        var participant = personRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException("Participante não encontrado com o ID: " + participantId));

        var enrollment = new Enrollment();
        enrollment.setEvent(existentEvent);
        enrollment.setParticipant(participant);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.CONFIRMADA);
        var savedEnrollment = enrollmentRepository.save(enrollment);

        String numericCode = "SUB" + String.format("%06d", savedEnrollment.getId());
        savedEnrollment.setNumericCode(numericCode);
        var finalEnrollment = enrollmentRepository.save(savedEnrollment);

        return enrollmentRepository.save(finalEnrollment);
    }

    public Page<Enrollment> getAllEnrollmentsByEventId(Long eventId, Integer page, Integer pageSize) {
        var enrollments = enrollmentRepository.findEnrollmentsByEventId(eventId, PageRequest.of(page, pageSize));

        if (enrollments.isEmpty()) {
            throw new ListEmptyException("Não há inscrições ainda.");
        }

        return enrollments;
    }

    @Transactional
    public ValidationResponseDTO validateByNumericCode(Long eventId, String numericCode) {
        Enrollment enrollment = enrollmentRepository.findByEventIdAndNumericCode(eventId, numericCode)
                .orElseThrow(() -> new EntityNotFoundException("Código de inscrição inválido para este evento."));
        return performCheckIn(enrollment);
    }

    @Transactional
    public ValidationResponseDTO validateByToken(String token) {
        Enrollment enrollment = enrollmentRepository.findByValidationToken(token)
                .orElseThrow(() -> new EntityNotFoundException("QR Code inválido ou inscrição não encontrada."));

        return performCheckIn(enrollment);
    }

    private ValidationResponseDTO performCheckIn(Enrollment enrollment) {
        if (enrollment.getWasPresent() == WasPresent.TRUE) {
            return new ValidationResponseDTO("Presença já registrada.",
                    enrollment.getParticipant().getName(),
                    enrollment.getEvent().getName(),
                    "JÁ REGISTRADO"
            );
        }

        var event = enrollment.getEvent();
        var now = LocalDate.now();

        if (now.isAfter(event.getInitialDate()) && now.isBefore(event.getFinalDate())) {
            enrollment.setWasPresent(WasPresent.TRUE);
            enrollmentRepository.save(enrollment);
            return new ValidationResponseDTO("Check-in realizado com sucesso!",
                    enrollment.getParticipant().getName(),
                    enrollment.getEvent().getName(),
                    "REGISTRADO"
            );
        } else {
            String message;
            String status;
            if (now.isBefore(event.getInitialDate())) {
                message = "O check-in para este evento ainda não começou.";
                status = "EVENTO NÃO INICIADO";
            } else {
                message = "O período de check-in para este evento já encerrou.";
                status = "EVENTO ENCERRADO";
            }

            return new ValidationResponseDTO(
                    message,
                    enrollment.getParticipant().getName(),
                    enrollment.getEvent().getName(),
                    status
            );
        }
    }
}
