package douglas.events.business.service;

import douglas.events.business.service.dto.QrCodeData;
import douglas.events.infraestructure.model.Enrollment;
import jakarta.activation.DataHandler;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final QrCodeService qrCodeService;

    public void sendQrCodeEmail(Enrollment enrollment) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(enrollment.getParticipant().getEmail());
            helper.setSubject("Seu QR Code para o Evento: " + enrollment.getEvent().getName());

            var qrCodeData = new QrCodeData(
                    enrollment.getId(),
                    enrollment.getEvent().getName(),
                    enrollment.getParticipant().getName(),
                    enrollment.getValidationToken(),
                    enrollment.getNumericCode()
            );
            byte[] qrCodeImage = qrCodeService.generateQrCode(qrCodeData, 400, 400);
            String imageContentId = "qrcodeImage";

            String htmlContent = "<h1>Olá, " + enrollment.getParticipant().getName() + "!</h1>"
                    + "<h2>Sua inscrição para o evento <strong>" + enrollment.getEvent().getName() + "</strong> foi confirmada.</h2>"
                    + "<p>Apresente o QR Code abaixo na entrada do evento.</p>"
                    + "<img src='cid:" + imageContentId + "' alt='QR Code do Evento' />" // O 'cid' é o Content-ID
                    + "<div style='border: 1px solid #ddd; padding: 15px; margin-top: 20px; text-align: center; background-color: #f9f9f9;'>"
                    + "  <p style='margin:0; font-family: sans-serif;'>Se não puder usar o QR Code, anote o seu código de check-in:</p>"
                    + "  <h2 style='font-size: 32px; letter-spacing: 5px; color: #000; margin: 10px 0;'>"
                    +      enrollment.getNumericCode()
                    + "  </h2>"
                    + "</div>";
            helper.setText(htmlContent, true);
            helper.addInline(imageContentId, new ByteArrayResource(qrCodeImage), "image/png");
            helper.addAttachment("qrcode.png", new ByteArrayResource(qrCodeImage));
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar e-mail com QR Code.", e);
        }
    }

    public void sendCertificateEmail(Enrollment enrollment, byte[] pdfAttachment) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(enrollment.getParticipant().getEmail());
            helper.setSubject("Seu Certificado do Evento: " + enrollment.getEvent().getName());

            var certificate = enrollment.getCertificate();
            String htmlContent = "<h1>Parabéns, " + enrollment.getParticipant().getName() + "!</h1>"
                    + "<h2>Seu certificado de participação no evento '" + enrollment.getEvent().getName() + "' foi gerado com sucesso e está em anexo.</h2>"
                    + "<p>Agradecemos a sua presença.</p>"
                    + "<p>Código de validação do seu certificado: <strong>" + certificate.getValidationCode() + "</strong></p>";

            helper.setText(htmlContent, true);

            helper.addAttachment("certificado.pdf", new ByteArrayResource(pdfAttachment));

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar e-mail de certificado.", e);
        }
    }
}