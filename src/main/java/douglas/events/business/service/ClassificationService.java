package douglas.events.business.service;

import douglas.events.infraestructure.exception.local.NotFoundException;
import douglas.events.infraestructure.model.Classification;
import douglas.events.infraestructure.repository.ClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassificationService {

    private final ClassificationRepository classificationRepository;

    public Classification createClassification(Classification classification) {
        return classificationRepository.save(classification);
    }

    public List<Classification> findClassificationByType(String type) {
        var classification = classificationRepository.findByCoClassificationType(type);
        if (classification == null) {
            throw new NotFoundException("Não há classificação para este tipo.");
        }

        return classification;
    }
}
