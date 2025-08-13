package douglas.events.business.service.dto;

public record QrCodeData(
    Long enrollmentId,
    String eventName,
    String participantName,
    String uniqueToken,
    String numericCode
) {}