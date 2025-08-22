package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PersonRepository extends JpaRepository<Person, Long> {
    Page<Person> findByRole(Role role, Pageable pageable);
    Optional<Person> findByEmail(String email);
}
