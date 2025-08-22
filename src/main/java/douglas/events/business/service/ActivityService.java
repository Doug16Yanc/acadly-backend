package douglas.events.business.service;


import douglas.events.application.dto.ActivityDto;
import douglas.events.application.dto.response.ApiResponse;
import douglas.events.application.dto.response.PaginationResponse;
import douglas.events.infraestructure.exception.local.DateConflictException;
import douglas.events.infraestructure.exception.local.EventNotFoundException;
import douglas.events.infraestructure.exception.local.NotFoundException;
import douglas.events.infraestructure.model.Activity;
import douglas.events.infraestructure.repository.ActivityRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final EventService eventService;

    public Activity createActivity(Long eventId, Activity activity) {
        var existentEvent = eventService.getEventEntityById(eventId);

        if (existentEvent == null) {
            throw new EventNotFoundException("Evento não encontrado com o ID: " + eventId);
        }

        if (activity.getDateTime().isBefore(existentEvent.getInitialDateTime()) || activity.getDateTime().isAfter(existentEvent.getFinalDateTime())) {
            throw new DateConflictException("Datas inválidas para a atividade");
        }
        activity.setEvent(existentEvent);

        return activityRepository.save(activity);
    }

    public Page<Activity> findAllActivitiesByEvent(Long eventId, Integer page, Integer pageSize) {
        Sort sort = Sort.by("dateTime").ascending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return activityRepository.findAllActivitiesByEventId(eventId, pageable);
    }

    public Page<Activity> findAllActivitiesByActiveEvent(Integer page, Integer pageSize) {
        Sort sort = Sort.by("dateTime").ascending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        var activityList = activityRepository.findAll(pageable);

        var activities = activityList.stream()
                .filter(activity -> activity.getEvent() != null && activity.getEvent().getIsActive())
                .toList();

        return new PageImpl<>(activities, pageable, activities.size());
    }

    public Activity getActivityById(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Atividade não encontrada"));
    }

    public Activity updateActivity(Long activityId, ActivityDto activityDto) {
        var activity = getActivityById(activityId);

        if (activityDto.name() != null) {
            activity.setName(activityDto.name());
        }
        if (activityDto.description() != null) {
            activity.setDescription(activityDto.description());
        }
        if (!Objects.equals(activityDto.duration(), activity.getDuration())) {
            activity.setDuration(activityDto.duration());
        }
        if (activity.getDateTime() != null) {
            activity.setDateTime(activityDto.dateTime());
        }
        if (activityDto.local() != null) {
            activity.setLocal(activityDto.local());
        }
        return activityRepository.save(activity);
    }

    public void deleteActivity(Long activityId) {
        activityRepository.deleteById(activityId);
    }

    @NotNull
    public ResponseEntity<ApiResponse<Activity>> getApiResponseEntity(Page<Activity> activities) {
        List<Activity> activityList = new ArrayList<>(List.of());
        activities.forEach(activityList::add);

        var response = new ApiResponse<>(
                activityList,
                new PaginationResponse(
                        activities.getNumber(),
                        activities.getSize(),
                        activities.getTotalElements(),
                        activities.getTotalPages()
                )
        );
        return ResponseEntity.ok(response);
    }
}
