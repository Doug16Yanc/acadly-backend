package douglas.events.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import douglas.events.infraestructure.model.Activity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ActivityDto(
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy:hh:mm:ss")
        LocalDateTime dateTime,
        Integer duration,
        String local
) {
    public static Activity toEntity(ActivityDto activityDto) {
        var activity = new Activity();
        activity.setName(activityDto.name());
        activity.setDescription(activityDto.description());
        activity.setDateTime(activityDto.dateTime());
        activity.setDuration(activityDto.duration());
        activity.setLocal(activityDto.local());

        return activity;
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
