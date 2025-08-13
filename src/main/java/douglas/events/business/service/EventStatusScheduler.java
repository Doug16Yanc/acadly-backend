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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    public void processStartEvents() {
        log.info("Executando verificação de eventos para iniciar...");

        var eventsToStart = eventRepository.findByStatusAndInitialDateBefore(EventStatus.UPCOMING, LocalDate.now());

        if (eventsToStart.isEmpty()) {
            log.info("Nenhum evento novo para iniciar.");
            return;
        }

        for (Event event : eventsToStart) {
            log.info("Iniciando evento: {}", event.getName());

            event.setStatus(EventStatus.STARTED);
            event.setActive(true);
            eventRepository.save(event);

            log.info("Evento '{}' marcado como ATIVO.", event.getName());
        }
    }


    @Scheduled(fixedRateString = "${event.processing.rate:60000}")
    @Transactional
    public void processFinishedEvents() {
        log.info("Executando verificação de eventos finalizados...");

        var finishedEvents = eventRepository.findByStatusAndFinalDateLessThanEqual(EventStatus.STARTED, LocalDate.now());

        if (finishedEvents.isEmpty()) {
            log.info("Nenhum evento novo para processar.");
            return;
        }

        for (Event event : finishedEvents) {
            log.info("Processando ausências para o evento: {}", event.getName());

            var pendingEnrollments = enrollmentRepository.findByEventAndWasPresent(event, WasPresent.PENDING);

            pendingEnrollments.forEach(enrollment -> enrollment.setWasPresent(WasPresent.FALSE));
            
            enrollmentRepository.saveAll(pendingEnrollments);
            
            log.info("{} participantes marcados como ausentes.", pendingEnrollments.size());

            event.setStatus(EventStatus.PROCESSED);
            event.setActive(false);
            eventRepository.save(event);
            
            log.info("Evento '{}' marcado como processado.", event.getName());
        }
    }

    @Scheduled(fixedRateString = "${certificate.processing.rate:60000}")
    @Transactional
    public void generateAndSendCertificates() {
        log.info("Executando verificação de certificados para gerar...");
        List<Event> eventsReadyForCerts = eventRepository.findByStatus(EventStatus.PROCESSED);

        for (Event event : eventsReadyForCerts) {
            log.info("Disparando geração de certificados para o evento: {}", event.getName());
            var attendees = enrollmentRepository.findByEventAndWasPresentAndCertificateIsNull(event, WasPresent.TRUE);

            for (Enrollment enrollment : attendees) {
                Certificate newCertificate = new Certificate();
                newCertificate.setEnrollment(enrollment);
                enrollment.setCertificate(newCertificate);
                enrollmentRepository.save(enrollment);

                asyncCertificateService.generateAndSendSingleCertificate(enrollment);
            }

            if (!attendees.isEmpty()) {
                event.setStatus(EventStatus.FINISHED);
                eventRepository.save(event);
                log.info("Geração de certificados para o evento '{}' disparada. Status atualizado.", event.getName());
            }
        }
    }
}