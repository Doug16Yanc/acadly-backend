package douglas.events.infraestructure.repository;

import douglas.events.application.dto.EventDto;
import douglas.events.infraestructure.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findEventByIsActive(boolean isActive);
}
