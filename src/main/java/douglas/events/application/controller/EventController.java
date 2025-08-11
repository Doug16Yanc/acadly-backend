package douglas.events.application.controller;

import douglas.events.application.dto.EventDto;
import douglas.events.business.service.EventService;
import douglas.events.infraestructure.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/create-event")
    public ResponseEntity<String> createEvent(@RequestBody EventDto eventDto) {
        var newEvent = eventService.createEvent(eventDto);

        return ResponseEntity.ok("Evento criado com sucesso" + newEvent.getName());
    }

    @GetMapping("/get-event/{eventId}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long eventId) {
        var eventFound = eventService.getEventById(eventId);
        return ResponseEntity.ok(eventFound);
    }

    @GetMapping("/get-event-active")
    public ResponseEntity<EventDto> getEventActive() {
        var eventFound = eventService.getActiveEvent();
        return ResponseEntity.ok(eventFound);
    }

    @GetMapping("/get-all-events")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
}
