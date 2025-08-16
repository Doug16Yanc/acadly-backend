package douglas.events.application.controller;

import douglas.events.application.dto.ActivityDto;
import douglas.events.application.dto.response.ApiResponse;
import douglas.events.business.service.ActivityService;
import douglas.events.infraestructure.model.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping("/create-activity/{eventId}")
    public ResponseEntity<String> createActivity(@PathVariable Long eventId, @RequestBody ActivityDto activityDto) {

        var newActivity = activityService.createActivity(eventId, ActivityDto.toEntity(activityDto));
        return ResponseEntity.ok().body("Atividade criada com sucesso para o evento " + newActivity.getEvent().getName());
    }

    @GetMapping("/get-all-activities/{eventId}")
    public ResponseEntity<ApiResponse<Activity>> getAllActivities(
            @PathVariable Long eventId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var listActivities = activityService.findAllActivitiesByEvent(eventId, page, pageSize);

        return activityService.getApiResponseEntity(listActivities);
    }

    @GetMapping("/get-all-activities-by-event")
    public ResponseEntity<ApiResponse<Activity>> getAllActivitiesByActiveEvent(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var listActivities = activityService.findAllActivitiesByActiveEvent(page, pageSize);

        return activityService.getApiResponseEntity(listActivities);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        var activity = activityService.getActivityById(id);
        return ResponseEntity.ok().body(activity);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody ActivityDto activityDto) {
        var activity = activityService.updateActivity(id, activityDto);
        return ResponseEntity.ok().body(activity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.ok().build();
    }
}
