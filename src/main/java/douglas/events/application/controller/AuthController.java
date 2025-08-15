package douglas.events.application.controller;

import douglas.events.application.dto.request.AuthAdminRequestDto;
import douglas.events.application.dto.request.AuthAdminResponseDto;
import douglas.events.application.dto.request.CreateAdminDto;
import douglas.events.business.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody CreateAdminDto createAdminDto) {
        var newAdmin = authService.createAdmin(createAdminDto);

        return ResponseEntity.ok().body("Administrador " + newAdmin.getUsername() + " criado com sucesso.");

    }

    @PostMapping("/login")
    public ResponseEntity<AuthAdminResponseDto> authenticateAdmin(@RequestBody AuthAdminRequestDto requestDto) {
        var admin = authService.authenticateAdmin(requestDto.email(), requestDto.password());
        return ResponseEntity.ok().body(admin);
    }
}