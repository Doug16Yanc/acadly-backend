package douglas.events.application.dto;

public record ManualValidationRequest(
        Long eventId,
        String numericCode
) {}
