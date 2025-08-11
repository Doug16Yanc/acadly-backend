package douglas.events.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import douglas.events.infraestructure.model.Activity;
import douglas.events.infraestructure.model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ActivityDto(
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dateTime,
        int duration,
        String local
) {
    public static Activity toEntity(Event event, ActivityDto activityDto) {

        return new Activity(
                null,
                event,
                activityDto.name,
                activityDto.description,
                activityDto.dateTime,
                activityDto.duration,
                activityDto.local
        );
    }

    public static ActivityDto fromEntity(Activity activity) {
        return new ActivityDto(
                activity.getName(),
                activity.getDescription(),
                activity.getDateTime(),
                activity.getDuration(),
                activity.getLocal()
        );
    }
}
