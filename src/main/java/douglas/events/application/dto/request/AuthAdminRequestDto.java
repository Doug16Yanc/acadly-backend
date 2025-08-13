package douglas.events.application.dto.request;

public record AuthAdminRequestDto(
        String username,
        String password
) { }