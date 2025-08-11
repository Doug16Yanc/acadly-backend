package douglas.events.application.controller;

import douglas.events.application.dto.AuthAdminRequestDto;
import douglas.events.application.dto.AuthAdminResponseDto;
import douglas.events.application.dto.CreateAdminDto;
import douglas.events.business.service.AuthService;
import douglas.events.infraestructure.config.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerEmployee(@RequestBody CreateAdminDto createAdminDto) {
        var newAdmin = authService.createAdmin(createAdminDto);

        return ResponseEntity.ok().body("Administrador " + newAdmin.getUsername() + " criado com sucesso.");

    }

    @PostMapping("/login")
    public ResponseEntity<AuthAdminResponseDto> authenticateEmployee(@RequestBody AuthAdminRequestDto requestDto) {
        var admin = authService.authenticateAdmin(requestDto.username(), requestDto.password());
        return ResponseEntity.ok().body(admin);
    }
}