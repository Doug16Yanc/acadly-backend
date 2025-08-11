package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findEnrollmentsByEventId(Long eventId);
}
