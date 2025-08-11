package douglas.events.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import douglas.events.infraestructure.model.Event;

import java.time.LocalDate;
import java.util.ArrayList;

public record EventDto(
        Long id,
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate initialDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate finalDate,
        String local,
        boolean isActive
) {
    public static Event toEntity(EventDto eventDto) {
        return new Event(
         null,
            eventDto.name(),
            eventDto.description(),
            eventDto.initialDate,
            eventDto.finalDate(),
            eventDto.local(),
            eventDto.isActive(),
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    public static EventDto fromEntity(Event event) {
        return new EventDto(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getInitialDate(),
                event.getFinalDate(),
                event.getLocal(),
                event.isActive()
        );
    }
}
