package douglas.events.business.service;

import douglas.events.infraestructure.model.Enrollment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncQRCodeService {

    private final EmailService emailService;

    @Async
    public void generateAndSendSingleQRCode(Enrollment enrollment) {
        log.info("Iniciando processo assíncrono para o participante: {}", enrollment.getParticipant().getName());
        try {
            log.info("Arquivo QR Code gerado na memória para {}.", enrollment.getParticipant().getName());

            emailService.sendQrCodeEmail(enrollment);
            log.info("Email com QR Code enviado para: {}", enrollment.getParticipant().getEmail());
        } catch (Exception e) {
            log.error("Falha no processo assíncrono para o participante ID {}: {}", enrollment.getParticipant().getId(), e.getMessage());
        }
    }
}