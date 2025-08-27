package douglas.events.business.service;

import douglas.events.infraestructure.model.Enrollment;
import douglas.events.infraestructure.repository.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncCertificateService {

    private final CertificatePdfService certificatePdfService;
    private final EnrollmentRepository enrollmentRepository;
    private final EmailService emailService;

    @Async
    @Transactional()
    public void generateAndSendSingleCertificate(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Inscrição não encontrada: " + enrollmentId));

        log.info("Iniciando processo assíncrono para o participante: {}", enrollment.getParticipant().getName());
        try {
            byte[] pdfData = certificatePdfService.generateCertificatePdf(enrollment);
            log.info("Arquivo PDF gerado na memória para {}.", enrollment.getParticipant().getName());

            emailService.sendCertificateEmail(enrollment, pdfData);

            var certificate = enrollment.getCertificate();
            certificate.setEmailSent(true);
            log.info("Email com certificado enviado para: {}", enrollment.getParticipant().getEmail());
        } catch (Exception e) {
            log.error("Falha no processo assíncrono para o participante ID {}: {}", enrollment.getParticipant().getId(), e.getMessage());
        }
    }
}