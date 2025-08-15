package douglas.events.application.dto.request;

public record CreateAdminDto(
       String name,
       String email,
       String password
) {}

