package douglas.events.application.dto.request;

public record CreateEmployeeDto(
    String name,
    String email,
    String password
) {}