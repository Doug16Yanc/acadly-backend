package douglas.events.application.dto.response;

public record ValidationResponseDTO(
        String message,
        String participantName,
        String eventName,
        String status
) {
}