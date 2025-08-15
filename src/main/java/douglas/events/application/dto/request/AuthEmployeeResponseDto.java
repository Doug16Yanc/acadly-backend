package douglas.events.application.dto.request;

public record AuthEmployeeResponseDto(
        String token,
        Long id,
        String name
) {
}