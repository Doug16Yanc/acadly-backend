package douglas.events.business.service;


import douglas.events.application.dto.response.ApiResponse;
import douglas.events.application.dto.response.PaginationResponse;
import douglas.events.infraestructure.exception.local.DateConflictException;
import douglas.events.infraestructure.exception.local.EventNotFoundException;
import douglas.events.infraestructure.exception.local.ListEmptyException;
import douglas.events.infraestructure.model.Activity;
import douglas.events.infraestructure.repository.ActivityRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        if (activity.getDateTime().isBefore(existentEvent.getInitialDate()) || activity.getDateTime().isAfter(existentEvent.getFinalDate())) {
            throw new DateConflictException("Datas inválidas para a atividade");
        }
        activity.setEvent(existentEvent);

        return activityRepository.save(activity);
    }

    public Page<Activity> findAllActivitiesByEvent(Long eventId, Integer page, Integer pageSize) {
        var activities = activityRepository.findAllActivitiesByEventId(eventId, PageRequest.of(page, pageSize));

        if (activities.isEmpty()) {
            throw new ListEmptyException("Não há atividades para o evento em questão.");
        }

        return activities;
    }

    public Page<Activity> findAllActivitiesByActiveEvent(Integer page, Integer pageSize) {
        var activityList = activityRepository.findAll(PageRequest.of(page, pageSize));

        if (activityList.isEmpty()) {
            throw new ListEmptyException("Não há atividades para o evento em questão.");
        }

        var activities = activityList.stream()
                .filter(activity -> activity.getEvent() != null && activity.getEvent().isActive())
                .toList();

        return new PageImpl<>(activities, PageRequest.of(page, pageSize), activities.size());
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
