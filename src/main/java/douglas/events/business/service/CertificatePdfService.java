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
                    acroForm.getField("participant_name").setValue(enrollment.getParticipant().getName());
                    acroForm.getField("event_name").setValue(enrollment.getEvent().getName());
                    acroForm.getField("certificate_code").setValue(enrollment.getCertificate().getValidationCode());

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String emissionDate = enrollment.getCertificate().getEmissionDate().format(formatter);
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