package douglas.events.business.listener;

import douglas.events.business.service.AsyncQRCodeService;
import douglas.events.business.service.dto.ParticipantCreatedEventDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class EmailNotificationListener {

    private final AsyncQRCodeService asyncQRCodeService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onParticipantCreated(ParticipantCreatedEventDTO event) {
        try {
            asyncQRCodeService.generateAndSendSingleQRCode(event.enrollment());
        } catch (Exception e) {

            System.err.println("Falha ao enviar e-mail para " + event.enrollment().getParticipant().getEmail() + ": " + e.getMessage());
        }
    }
}