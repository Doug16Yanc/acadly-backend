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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final QrCodeService qrCodeService;

    public void sendQrCodeEmail(Enrollment enrollment) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(enrollment.getParticipant().getEmail()));
            message.setSubject("Seu QR Code para o Evento: " + enrollment.getEvent().getName());

            var qrCodeData = new QrCodeData(
                    enrollment.getId(),
                    enrollment.getEvent().getName(),
                    enrollment.getParticipant().getName(),
                    enrollment.getValidationToken(),
                    enrollment.getNumericCode()
            );

            byte[] qrCodeImage = qrCodeService.generateQrCode(qrCodeData, 400, 400);

            String imageContentId = "qrcodeImage";

            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = "<h1>Olá, " + enrollment.getParticipant().getName() + "!</h1>"
                    + "<h2>Sua inscrição para o evento '" + enrollment.getEvent().getName() + "' foi confirmada.</h2>"
                    + "<p>Apresente o QR Code abaixo na entrada do evento.</p>"
                    + "<img src='cid:" + imageContentId + "' alt='QR Code do Evento' />"
                    + "<div style='border: 1px solid #ddd; padding: 15px; margin-top: 20px; text-align: center; background-color: #f9f9f9;'>"
                    + "  <p style='margin:0; font-family: sans-serif;'>Se não puder usar o QR Code, anote o seu código de check-in:</p>"
                    + "  <h2 style='font-size: 32px; letter-spacing: 5px; color: #000; margin: 10px 0;'>"
                    +      enrollment.getNumericCode()
                    + "  </h2>"
                    + "</div>";
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");

            MimeBodyPart imagePart = new MimeBodyPart();
            ByteArrayDataSource inlineDataSource = new ByteArrayDataSource(qrCodeImage, "image/png");
            imagePart.setDataHandler(new DataHandler(inlineDataSource));
            imagePart.setHeader("Content-ID", "<" + imageContentId + ">");
            imagePart.setDisposition(MimeBodyPart.INLINE);

            Multipart multipartRelated = new MimeMultipart("related");
            multipartRelated.addBodyPart(htmlPart);
            multipartRelated.addBodyPart(imagePart);

            MimeBodyPart relatedBodyPart = new MimeBodyPart();
            relatedBodyPart.setContent(multipartRelated);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            ByteArrayDataSource attachmentDataSource = new ByteArrayDataSource(qrCodeImage, "image/png");
            attachmentPart.setDataHandler(new DataHandler(attachmentDataSource));
            attachmentPart.setDisposition(MimeBodyPart.ATTACHMENT);
            attachmentPart.setFileName("qrcode.png");

            Multipart multipartMixed = new MimeMultipart("mixed");
            multipartMixed.addBodyPart(relatedBodyPart);
            multipartMixed.addBodyPart(attachmentPart);

            message.setContent(multipartMixed);

            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCertificateEmail(Enrollment enrollment, byte[] pdfAttachment) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(enrollment.getParticipant().getEmail()));
            message.setSubject("Seu Certificado do Evento: " + enrollment.getEvent().getName());

            MimeBodyPart htmlPart = new MimeBodyPart();
            var certificate = enrollment.getCertificate();
            String htmlContent = "<h1>Parabéns, " + enrollment.getParticipant().getName() + "!</h1>"
                    + "<h2>Seu certificado de participação no evento '" + enrollment.getEvent().getName() + "' foi gerado com sucesso e está em anexo.</h2>"
                    + "<p>Agradecemos a sua presença.</p>"
                    + "<p>Código de validação do seu certificado: <strong>" + certificate.getValidationCode() + "</strong></p>";
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfAttachment, "application/pdf");
            attachmentPart.setDataHandler(new DataHandler(dataSource));
            attachmentPart.setDisposition(MimeBodyPart.ATTACHMENT);
            attachmentPart.setFileName("certificado.pdf");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}