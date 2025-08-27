package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.model.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByIsActiveTrue();
    Event findEventByIsActive(boolean isActive);
    List<Event> findByStatusAndFinalDateTimeLessThanEqual(EventStatus status, LocalDateTime currentDateTime);
    List<Event> findByInitialDateTimeAfter(LocalDateTime currentDateTime);
    List<Event> findByStatus(EventStatus status);
    Optional<Event> findTopByIsActiveTrueOrderByFinalDateTimeDesc();
    Optional<Event> findTopByOrderByFinalDateTimeDesc();
    Page<Event> findByNameContainingIgnoreCase(String title, Pageable pageable);
    List<Event> findByStatusAndInitialDateTimeLessThanEqualAndFinalDateTimeGreaterThanEqual(
            EventStatus status,
            LocalDateTime initialDateTime,
            LocalDateTime finalDateTime
    );

    @Query(
            value = "SELECT p.* FROM person AS p JOIN enrollments AS e ON p.id=e.participant_id WHERE e.event_id = ?1",
            countQuery = "SELECT count(*) FROM person AS p JOIN enrollments AS e ON p.id = e.participant_id WHERE e.event_id = ?1",
            nativeQuery = true
    )
    Page<Person> findAllParticipantsByEvent(Long eventId, Pageable pageable);
}
