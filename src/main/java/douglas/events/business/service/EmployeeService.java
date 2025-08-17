package douglas.events.business.service;

import douglas.events.application.dto.request.CreateOrUpdateEmployeeDto;
import douglas.events.infraestructure.exception.local.*;
import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.model.enums.Role;
import douglas.events.infraestructure.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    public Page<Person> getAllEmployees(Integer page, Integer pageSize) {
        return personRepository.findByRole(Role.EMPLOYEE, PageRequest.of(page, pageSize));
    }

    public Person getEmployeeById(Long employeeId) {
        var person = personRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID: " + employeeId));

        if (person.getRole() != Role.EMPLOYEE) {
            throw new NotFoundException("Funcionário não encontrado com o ID: " + employeeId);
        }

        return person;
    }

    public Person getEmployeeByEmail(String employeeEmail) {
        var person = personRepository.findByEmail(employeeEmail);

        if (person.getRole() != Role.EMPLOYEE) {
            throw new NotFoundException("Funcionário não encontrado com o email: " + employeeEmail);
        }

        return person;
    }

    @Transactional
    public Person createEmployee(CreateOrUpdateEmployeeDto dto) {
        var person = personRepository.findByEmail(dto.email());

        if (person != null){
            throw new UsernameAlreadyExistsException("O email informado já está em uso.");
        }

        var employee = new Person();
        employee.setName(dto.name());
        employee.setEmail(dto.email());
        employee.setPassword(passwordEncoder.encode(dto.password()));
        employee.setRole(Role.EMPLOYEE);

        return personRepository.save(employee);
    }

    @Transactional
    public Person updateEmployee(Long employeeId, CreateOrUpdateEmployeeDto dto) {
        var employeeToUpdate = getEmployeeById(employeeId);

        var personWithNewEmail = personRepository.findByEmail(dto.email());
        if (personWithNewEmail != null && !personWithNewEmail.getId().equals(employeeId)) {
            throw new UsernameAlreadyExistsException("O email informado já está em uso por outro usuário.");
        }

        employeeToUpdate.setName(dto.name());
        employeeToUpdate.setEmail(dto.email());

        return personRepository.save(employeeToUpdate);
    }

    @Transactional
    public void deleteEmployee(Long employeeId) {
        var employeeToDelete = getEmployeeById(employeeId);
        personRepository.delete(employeeToDelete);
    }
}
