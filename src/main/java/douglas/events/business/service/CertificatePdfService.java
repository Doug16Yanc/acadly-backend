package douglas.events.business.service;

import douglas.events.infraestructure.model.Enrollment;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class CertificatePdfService {

    public byte[] generateCertificatePdf(Enrollment enrollment) throws IOException {
        String templatePath = "/template.pdf";

        try (InputStream templateInputStream = getClass().getResourceAsStream(templatePath)) {
            if (templateInputStream == null) {
                throw new IOException("Arquivo de template não encontrado: " + templatePath);
            }

            try (PDDocument pdfDocument = Loader.loadPDF(templateInputStream.readAllBytes())) {

                PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

                if (acroForm != null) {

                    Locale localeBrazil = Locale.forLanguageTag("pt-BR");
                    DateTimeFormatter eventDateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy HH:mm", localeBrazil);
                    String initialDate = enrollment.getEvent().getInitialDateTime().format(eventDateFormatter);
                    String finalDate = enrollment.getEvent().getFinalDateTime().format(eventDateFormatter);

                    DateTimeFormatter emissionDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", localeBrazil);
                    String emissionDate = enrollment.getCertificate().getEmissionDate().format(emissionDateFormatter) + " Boa Viagem - CE";

                    String participantName = enrollment.getParticipant().getName();
                    String eventName = enrollment.getEvent().getName();
                    Integer workload = enrollment.getEvent().getWorkload();
                    String coordinatorName = enrollment.getEvent().getCoordinator();
                    String certificateCode = enrollment.getCertificate().getValidationCode();
                    String local = enrollment.getEvent().getLocal();

                    String certificateText = String.format(
                            "Certificamos que %s participou do evento '%s', realizado no %s no período de %s a %s, com carga horária total de %d horas.",
                            participantName,
                            eventName,
                            local,
                            initialDate,
                            finalDate,
                            workload
                    );

                    acroForm.getField("text_template").setValue(certificateText);
                    acroForm.getField("coordinator").setValue(coordinatorName);
                    acroForm.getField("certificate_code").setValue(certificateCode);
                    acroForm.getField("emission_date").setValue(emissionDate);

                    acroForm.flatten();
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                pdfDocument.save(outputStream);
                return outputStream.toByteArray();
            }
        }
    }
}