package douglas.events.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.model.enums.EventStatus;

import java.time.LocalDateTime;

public record EventResponseDTO(
        Long id,
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy:hh:mm:ss")
        LocalDateTime initialDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy:hh:mm:ss")
        LocalDateTime finalDateTime,
        String local,
        EventStatus status,
        Integer workload,
        Boolean isActive
) {

    public static EventResponseDTO fromEntity(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getInitialDateTime(),
                event.getFinalDateTime(),
                event.getLocal(),
                event.getStatus(),
                event.getWorkload(),
                event.getIsActive()
        );
    }

}