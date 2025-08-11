package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllActivitiesByEventId(Long eventId);
}
