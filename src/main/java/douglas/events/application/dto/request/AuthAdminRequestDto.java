package douglas.events.application.dto.request;

public record AuthAdminRequestDto(
        String email,
        String password
) { }