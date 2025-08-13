package douglas.events.business.service;

import douglas.events.application.dto.request.AuthAdminResponseDto;
import douglas.events.application.dto.CreateAdminDto;
import douglas.events.infraestructure.config.security.TokenService;
import douglas.events.infraestructure.exception.authentication.AuthAdminNotMatchesException;
import douglas.events.infraestructure.exception.local.NotFoundException;
import douglas.events.infraestructure.model.Administrator;
import douglas.events.infraestructure.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public Administrator createAdmin(CreateAdminDto createAdminDto) {
        var alreadyExistent = administratorRepository.findByUsername(createAdminDto.username());

        if (alreadyExistent != null) {
            throw new RuntimeException("Username already exists");
        }

        Administrator admin = new Administrator();
        admin.setUsername(createAdminDto.username());
        admin.setPassword(passwordEncoder.encode(createAdminDto.password()));

        Administrator savedAdmin = administratorRepository.save(admin);

        String jwtToken = tokenService.generateToken(savedAdmin.getUsername());

        return savedAdmin;
    }

    public Administrator findAdminByUsername(String username) {
        var admin = administratorRepository.findByUsername(username);
        if (admin == null) throw new NotFoundException("Administrador não encontrado.");
        return admin;
    }

    public AuthAdminResponseDto authenticateAdmin(String username, String rawPassword) {
        var admin = findAdminByUsername(username);
        if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
            throw new AuthAdminNotMatchesException();
        }
        var token = tokenService.generateToken(admin.getUsername());
        return new AuthAdminResponseDto(token, admin.getId(), admin.getUsername());
    }
}