package douglas.events.business.service;

import douglas.events.infraestructure.model.Certificate;
import douglas.events.infraestructure.model.Enrollment;
import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.model.enums.EventStatus;
import douglas.events.infraestructure.model.enums.WasPresent;
import douglas.events.infraestructure.repository.EnrollmentRepository;
import douglas.events.infraestructure.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventStatusScheduler {

    private final EventRepository eventRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AsyncCertificateService asyncCertificateService;

    @Scheduled(fixedRateString = "${event.processing.rate:60000}")
    @Transactional
    public void processUpcomingEvents() {
        log.info("Executando verificação de eventos para lançar...");

        var eventsToUpcoming = eventRepository.findByInitialDateTimeAfter(LocalDateTime.now());

        if (eventsToUpcoming.isEmpty()) {
            log.info("Nenhum evento novo para lançar.");
            return;
        }

        for (Event event : eventsToUpcoming) {
            log.info("Verificando status do evento: {}" , event.getName());

            if (event.getStatus() != EventStatus.UPCOMING) {
                log.info("Lançando evento: {}", event.getName());
                event.setStatus(EventStatus.UPCOMING);
                event.setIsActive(false);
                eventRepository.save(event);
                return;
            }
            log.info("Evento já em lançamento: {}" , event.getStatus());
        }
    }

    @Scheduled(fixedRateString = "${event.processing.rate:60000}")
    @Transactional
    public void processStartEvents() {
        log.info("Executando verificação de eventos para iniciar...");
        LocalDateTime now = LocalDateTime.now();

        var eventsToStart = eventRepository
                .findByStatusAndInitialDateTimeLessThanEqualAndFinalDateTimeGreaterThanEqual(
                EventStatus.UPCOMING, now, now
        );

        if (eventsToStart.isEmpty()) {
            log.info("Nenhum evento novo para iniciar.");
            return;
        }

        for (Event event : eventsToStart) {
            log.info("Verificando se o evento está ativo: {}", event.getName());
            if (event.getIsActive()) {
                log.info("Iniciando evento: {}", event.getName());
                event.setStatus(EventStatus.STARTED);
                eventRepository.save(event);
                log.info("Evento '{}' iniciado.", event.getName());
                return;
            }
            log.info("Evento não pode ser iniciado.");
        }
    }


    @Scheduled(fixedRateString = "${event.processing.rate:60000}")
    @Transactional
    public void processFinishedEvents() {
        log.info("Executando verificação de eventos finalizados...");

        var finishedEvents = eventRepository.findByStatusAndFinalDateTimeLessThanEqual(EventStatus.STARTED, LocalDateTime.now());

        if (finishedEvents.isEmpty()) {
            log.info("Nenhum evento finalizado para processar.");
            return;
        }

        for (Event event : finishedEvents) {
            log.info("Processando ausências para o evento: {}", event.getName());

            var pendingEnrollments = enrollmentRepository.findByEventAndWasPresent(event, WasPresent.PENDING);

            if (!pendingEnrollments.isEmpty()) {
                pendingEnrollments.forEach(enrollment -> enrollment.setWasPresent(WasPresent.FALSE));
                enrollmentRepository.saveAll(pendingEnrollments);
                log.info("{} participantes marcados como ausentes.", pendingEnrollments.size());
            } else {
                log.info("Nenhum participante com presença pendente para este evento.");
            }

            event.setStatus(EventStatus.PROCESSED);
            event.setIsActive(false);
            eventRepository.save(event);

            log.info("Evento '{}' marcado como processado.", event.getName());
        }
    }

    @Scheduled(fixedRateString = "${certificate.processing.rate:60000}")
    @Transactional
    public void generateAndSendCertificates() {
        var eventsReadyForCerts = eventRepository.findByStatus(EventStatus.PROCESSED);

        log.info("Executando verificação de certificados para gerar...");

        for (Event event : eventsReadyForCerts) {
            var existsParticipants = enrollmentRepository.findEnrollmentsByEventId(event.getId(), PageRequest.of(0, 1)).getTotalElements() > 0;
            if (!existsParticipants) {
                log.info("Nenhum participante encontrado para o evento: {}." , event.getName());
                event.setStatus(EventStatus.FINISHED);
                eventRepository.save(event);
                return;
            }

            log.info("Disparando geração de certificados para o evento: {}", event.getName());
            var attendees = enrollmentRepository.findByEventAndWasPresentAndCertificateIsNull(event, WasPresent.TRUE);

            for (Enrollment enrollment : attendees) {
                if (enrollment.getCertificate() == null) {
                    Certificate newCertificate = new Certificate();
                    newCertificate.setEnrollment(enrollment);
                    enrollment.setCertificate(newCertificate);
                    enrollmentRepository.save(enrollment);
                    log.info("Certificado criado para o participante ID {}.", enrollment.getParticipant().getId());
                }
            }

            if (!attendees.isEmpty()) {
                event.setStatus(EventStatus.FINISHED);
                eventRepository.save(event);
                log.info("Geração de certificados para o evento '{}' disparada. Status atualizado.", event.getName());
            }
        }
    }

    @Scheduled(fixedRateString = "${certificate.sending.rate:60000}", initialDelay = 30000)
    @Transactional
    public void sendCertificateEmails() {
        List<Enrollment> enrollmentsWithCerts = enrollmentRepository.findWithCertificateReadyToSend();

        log.info("Encontradas {} inscrições com certificados para enviar por e-mail.", enrollmentsWithCerts.size());

        for (Enrollment enrollment : enrollmentsWithCerts) {
            asyncCertificateService.generateAndSendSingleCertificate(enrollment.getId());
        }
    }
}