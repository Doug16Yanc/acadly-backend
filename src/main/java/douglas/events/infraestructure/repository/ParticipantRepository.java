package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
