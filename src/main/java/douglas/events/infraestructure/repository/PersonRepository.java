package douglas.events.infraestructure.repository;

import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person, Long> {
    Page<Person> findByRole(Role role, Pageable pageable);
    Person findByEmail(String email);

}
