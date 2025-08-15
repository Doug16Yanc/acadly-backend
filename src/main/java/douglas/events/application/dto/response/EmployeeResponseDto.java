package douglas.events.application.dto.response;

import douglas.events.infraestructure.model.Person;

public record EmployeeResponseDto(
    Long id,
    String name,
    String email
) {
    public static EmployeeResponseDto fromEntity(Person person) {
        return new EmployeeResponseDto(
            person.getId(),
            person.getName(),
            person.getEmail()
        );
    }
}