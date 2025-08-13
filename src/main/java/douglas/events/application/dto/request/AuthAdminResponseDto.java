package douglas.events.application.dto.request;

public record AuthAdminResponseDto(
        String token,
        Long id,
        String name
) {
}