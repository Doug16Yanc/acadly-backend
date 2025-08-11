package douglas.events.application.controller;

import douglas.events.application.dto.ActivityDto;
import douglas.events.business.service.ActivityService;
import douglas.events.infraestructure.model.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping("/create-activity/{eventId}")
    public ResponseEntity<String> createActivity(@PathVariable Long eventId, @RequestBody ActivityDto activityDto) {
        var newActivity = activityService.createActivity(eventId, activityDto);

        return ResponseEntity.ok().body("Atividade criada com sucesso para o evento " + newActivity.getEvent().getName());
    }

    @GetMapping("/get-all-activities/{eventId}")
    public ResponseEntity<List<Activity>> getAllActivities(@PathVariable Long eventId) {
        var listActivities = activityService.findAllActivitiesByEvent(eventId);

        return ResponseEntity.ok().body(listActivities);
    }

    @GetMapping("/get-all-activities-by-event")
    public ResponseEntity<List<ActivityDto>> getAllActivitiesByActiveEvent() {
        var listActivities = activityService.findAllActivitiesByActiveEvent();

        return ResponseEntity.ok().body(listActivities);
    }
}
