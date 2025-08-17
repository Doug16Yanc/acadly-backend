package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Enrollment;
import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.model.enums.WasPresent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query(
            value = "SELECT * FROM enrollments WHERE event_id = ?1 \n-- #pageable\n",
            countQuery = "SELECT count(*) FROM enrollments WHERE event_id = ?1",
            nativeQuery = true
    )
    Page<Enrollment> findEnrollmentsByEventId(Long eventId, Pageable pageable);
    Optional<Enrollment> findByValidationToken(String validationToken);
    List<Enrollment> findByEventAndWasPresent(Event event, WasPresent wasPresent);
    List<Enrollment> findByEventAndWasPresentAndCertificateIsNull(Event event, WasPresent wasPresent);
    Optional<Enrollment> findByEventIdAndNumericCode(Long eventId, String numericCode);
    Optional<Enrollment> findByEventAndParticipant(Event event, Person participant);
}
