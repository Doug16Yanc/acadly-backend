package douglas.events.business.service;

import douglas.events.infraestructure.model.Enrollment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncCertificateService {

    private final CertificatePdfService certificatePdfService;
    private final EmailService emailService;

    @Async
    public void generateAndSendSingleCertificate(Enrollment enrollment) {
        log.info("Iniciando processo assíncrono para o participante: {}", enrollment.getParticipant().getName());
        try {
            byte[] pdfData = certificatePdfService.generateCertificatePdf(enrollment);
            log.info("Arquivo PDF gerado na memória para {}.", enrollment.getParticipant().getName());

            emailService.sendCertificateEmail(enrollment, pdfData);
            log.info("Email com certificado enviado para: {}", enrollment.getParticipant().getEmail());
        } catch (Exception e) {
            log.error("Falha no processo assíncrono para o participante ID {}: {}", enrollment.getParticipant().getId(), e.getMessage());
        }
    }
}