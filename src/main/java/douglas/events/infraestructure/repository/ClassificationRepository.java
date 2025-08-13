package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Classification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassificationRepository extends JpaRepository<Classification, Long> {
    List<Classification> findByCoClassificationType(String type);
}
