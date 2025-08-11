package douglas.events.application.dto;

public record AuthAdminResponseDto(
        String token,
        Long id,
        String name
) {
}