package douglas.events.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import douglas.events.infraestructure.model.Event;

import java.time.LocalDateTime;

public record EventDto(
        String name,
        String description,
        LocalDateTime initialDateTime,
        LocalDateTime finalDateTime,
        String local,
        String coordinator,
        Integer workload,
        Boolean isActive
) {
    public static Event toEntity(EventDto eventDto) {
        var event = new Event();
        event.setName(eventDto.name);
        event.setDescription(eventDto.description);
        event.setInitialDateTime(eventDto.initialDateTime);
        event.setFinalDateTime(eventDto.finalDateTime);
        event.setLocal(eventDto.local);
        event.setCoordinator(eventDto.coordinator);
        event.setWorkload(eventDto.workload);
        event.setIsActive(eventDto.isActive);
        return event;
    }

    public static EventDto fromEntity(Event event) {
        return new EventDto(
                event.getName(),
                event.getDescription(),
                event.getInitialDateTime(),
                event.getFinalDateTime(),
                event.getLocal(),
                event.getCoordinator(),
                event.getWorkload(),
                event.getIsActive()
        );
    }
}
