package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findAllActivitiesByEventId(Long eventId, Pageable pageable);
}
