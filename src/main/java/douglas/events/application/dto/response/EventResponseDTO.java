package douglas.events.application.dto.response;

import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.model.enums.EventStatus;

import java.time.LocalDateTime;

public record EventResponseDTO(
        Long id,
        String name,
        String description,
        LocalDateTime initialDateTime,
        LocalDateTime finalDateTime,
        String local,
        EventStatus status,
        String coordinator,
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
                event.getCoordinator(),
                event.getWorkload(),
                event.getIsActive()
        );
    }

}