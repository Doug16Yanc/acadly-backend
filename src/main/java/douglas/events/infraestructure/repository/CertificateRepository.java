package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}
