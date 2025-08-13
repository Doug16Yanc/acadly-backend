package douglas.events.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.model.enums.EventStatus;

import java.time.LocalDate;

public record EventResponseDTO(
        Long id,
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate initialDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate finalDate,
        String local,
        EventStatus status,
        boolean isActive
) {

    public static EventResponseDTO fromEntity(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getInitialDate(),
                event.getFinalDate(),
                event.getLocal(),
                event.getStatus(),
                event.isActive()
        );
    }

}