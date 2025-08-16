package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.model.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findEventByIsActive(boolean isActive);
    List<Event> findByStatusAndFinalDateLessThanEqual(EventStatus status, LocalDate currentDate);
    List<Event> findByStatusAndInitialDateBefore(EventStatus status, LocalDate currentDate);
    List<Event> findByStatus(EventStatus status);
    Optional<Event> findTopByIsActiveTrueOrderByFinalDateDesc();
    Optional<Event> findTopByOrderByFinalDateDesc();
}
