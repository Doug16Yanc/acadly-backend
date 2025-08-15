package douglas.events.business.service;

import douglas.events.application.dto.request.AuthAdminResponseDto;
import douglas.events.application.dto.request.AuthEmployeeResponseDto;
import douglas.events.application.dto.request.CreateAdminDto;
import douglas.events.infraestructure.config.security.TokenService;
import douglas.events.infraestructure.exception.authentication.AuthNotMatchesException;
import douglas.events.infraestructure.exception.local.AdminNotFoundException;
import douglas.events.infraestructure.exception.local.EmployeeNotFoundException;
import douglas.events.infraestructure.exception.local.UsernameAlreadyExistsException;
import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.model.enums.Role;
import douglas.events.infraestructure.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public Person createAdmin(CreateAdminDto createAdminDto) {
        var alreadyExistent = personRepository.findByEmail(createAdminDto.email());

        if (alreadyExistent != null) {
            throw new UsernameAlreadyExistsException("Email já cadastrado.");
        }

        var admin = new Person();

        admin.setName(createAdminDto.name());
        admin.setEmail(createAdminDto.email());
        admin.setPassword(passwordEncoder.encode(createAdminDto.password()));
        admin.setRole(Role.ADMIN);

        var savedAdmin = personRepository.save(admin);
        tokenService.generateToken(savedAdmin.getUsername());

        return savedAdmin;
    }

    public Person findAdminByUsername(String email) {
        var admin = personRepository.findByEmail(email);
        if (admin == null) throw new AdminNotFoundException("Administrador não encontrado.");

        if (admin.getRole() != Role.ADMIN) {
            throw new AdminNotFoundException("Administrador não encontrado.");
        }
        return admin;
    }

    public AuthAdminResponseDto authenticateAdmin(String email, String rawPassword) {
        var admin = findAdminByUsername(email);
        if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
            throw new AuthNotMatchesException();
        }

        var token = tokenService.generateToken(admin.getUsername());
        return new AuthAdminResponseDto(token, admin.getId(), admin.getUsername());
    }

    public Person findEmployeeByUsername(String email) {
        var employee = personRepository.findByEmail(email);
        if (employee == null) throw new EmployeeNotFoundException("Funcionário não encontrado com o email: " + email);

        if (employee.getRole() != Role.EMPLOYEE) {
            throw new EmployeeNotFoundException("Funcionário não encontrado com o email: " + email);
        }
        return employee;
    }

    public AuthEmployeeResponseDto authenticateEmployee(String email, String rawPassword) {
        var employee = findEmployeeByUsername(email);
        if (!passwordEncoder.matches(rawPassword, employee.getPassword())) {
            throw new AuthNotMatchesException();
        }

        var token = tokenService.generateToken(employee.getUsername());
        return new AuthEmployeeResponseDto(token, employee.getId(), employee.getUsername());
    }

}