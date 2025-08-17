package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.model.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByIsActiveTrue();
    Event findEventByIsActive(boolean isActive);
    List<Event> findByStatusAndFinalDateLessThanEqual(EventStatus status, LocalDate currentDate);
    List<Event> findByInitialDateAfter(LocalDate currentDate);
    List<Event> findByStatus(EventStatus status);
    Optional<Event> findTopByIsActiveTrueOrderByFinalDateDesc();
    Optional<Event> findTopByOrderByFinalDateDesc();
    Page<Event> findByNameContainingIgnoreCase(String title, Pageable pageable);
    List<Event> findByStatusAndInitialDateLessThanEqualAndFinalDateGreaterThanEqual(
            EventStatus status,
            LocalDate initialDate,
            LocalDate finalDate
    );

    @Query(
            value = "SELECT p.* FROM person AS p JOIN enrollments AS e ON p.id=e.participant_id WHERE e.event_id = ?1",
            countQuery = "SELECT count(*) FROM person AS p JOIN enrollments AS e ON p.id = e.participant_id WHERE e.event_id = ?1",
            nativeQuery = true
    )
    Page<Person> findAllParticipantsByEvent(Long eventId, Pageable pageable);
}
