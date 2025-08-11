package douglas.events.business.service;

import douglas.events.application.dto.ActivityDto;
import douglas.events.infraestructure.exception.local.DateConflictException;
import douglas.events.infraestructure.exception.local.ListEmptyException;
import douglas.events.infraestructure.model.Activity;
import douglas.events.infraestructure.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final EventService eventService;

    public Activity createActivity(Long eventId, ActivityDto activityDto) {
        var existentEvent = eventService.getEventEntityById(eventId);

        if (activityDto.dateTime().isBefore(existentEvent.getInitialDate()) || activityDto.dateTime().isAfter(existentEvent.getFinalDate())) {
            throw new DateConflictException("Datas inválidas para a atividade");
        }

        var savedActivity = ActivityDto.toEntity(existentEvent, activityDto);

        return activityRepository.save(savedActivity);
    }

    public List<Activity> findAllActivitiesByEvent(Long eventId) {
        var activities = activityRepository.findAllActivitiesByEventId(eventId);

        if (activities.isEmpty()) {
            throw new ListEmptyException("Não há atividades para o evento em questão.");
        }

        return activities;
    }

    public List<ActivityDto> findAllActivitiesByActiveEvent() {
        var activities = activityRepository.findAll();

        if (activities.isEmpty()) {
            throw new ListEmptyException("Não há atividades para o evento em questão.");
        }

        return activities.stream()
                .filter(activity -> activity.getEvent() != null && activity.getEvent().isActive())
                .map(ActivityDto::fromEntity)
                .collect(Collectors.toList());
    }
}
