package douglas.events.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import douglas.events.infraestructure.model.Classification;

import java.util.List;

public interface ClassificationRepository extends JpaRepository<Classification, Long> {
    List<Classification> findByCoClassificationType(String type);
}
