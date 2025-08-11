package douglas.events.application.dto;

public record AuthAdminRequestDto(
        String username,
        String password
) { }