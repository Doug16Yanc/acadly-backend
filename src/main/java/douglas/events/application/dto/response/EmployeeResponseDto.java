package douglas.events.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import douglas.events.infraestructure.model.Person;

import java.time.LocalDateTime;

public record EmployeeResponseDto(
    Long id,
    String name,
    String email,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy:HH:mm:ss")
    LocalDateTime createdAt
) {
    public static EmployeeResponseDto fromEntity(Person person) {
        return new EmployeeResponseDto(
            person.getId(),
            person.getName(),
            person.getEmail(),
            person.getCreatedAt()
        );
    }
}