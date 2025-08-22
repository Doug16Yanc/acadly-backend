package douglas.events.application.controller;

import douglas.events.application.dto.EventDto;
import douglas.events.application.dto.response.ApiResponse;
import douglas.events.application.dto.response.EventResponseDTO;
import douglas.events.application.dto.response.ParticipantResponseDTO;
import douglas.events.business.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static douglas.events.application.util.UtilApi.getApiResponseEntity;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/create-event")
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventDto eventDto) {
        var newEvent = eventService.createEvent(EventDto.toEntity(eventDto));

        return ResponseEntity.ok(
                new EventResponseDTO(
                        newEvent.getId(),
                        newEvent.getName(),
                        newEvent.getDescription(),
                        newEvent.getInitialDateTime(),
                        newEvent.getFinalDateTime(),
                        newEvent.getLocal(),
                        newEvent.getStatus(),
                        newEvent.getWorkload(),
                        newEvent.getIsActive()
                )
        );
    }

    @GetMapping("/get-event/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable Long eventId) {
        var eventFound = eventService.getEventById(eventId);
        return ResponseEntity.ok(
                new EventResponseDTO(
                        eventFound.getId(),
                        eventFound.getName(),
                        eventFound.getDescription(),
                        eventFound.getInitialDateTime(),
                        eventFound.getFinalDateTime(),
                        eventFound.getLocal(),
                        eventFound.getStatus(),
                        eventFound.getWorkload(),
                        eventFound.getIsActive()
                )
        );
    }

    @GetMapping("/get-event-active")
    public ResponseEntity<EventResponseDTO> getEventActive() {
        var eventFound = eventService.getActiveEvent();
        return ResponseEntity.ok(
                new EventResponseDTO(
                        eventFound.getId(),
                        eventFound.getName(),
                        eventFound.getDescription(),
                        eventFound.getInitialDateTime(),
                        eventFound.getFinalDateTime(),
                        eventFound.getLocal(),
                        eventFound.getStatus(),
                        eventFound.getWorkload(),
                        eventFound.getIsActive()
                )
        );
    }

    @GetMapping("/get-all-events")
    public ResponseEntity<ApiResponse<EventResponseDTO>> getAllEvents(
            @RequestParam(name = "query", defaultValue = "") String query,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var events = eventService.getAllEvents(query, page, pageSize);
        return getApiResponseEntity(events, EventResponseDTO::fromEntity);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto) {
        var event = eventService.updateEvent(id, EventDto.toEntity(eventDto));

        return ResponseEntity.ok(EventResponseDTO.fromEntity(event));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists-active")
    public ResponseEntity<Boolean> existsActive() {
        var isActive = eventService.existsActive();
        return ResponseEntity.ok(isActive);
    }

    @GetMapping("/get-participants/{id}")
    public ResponseEntity<ApiResponse<ParticipantResponseDTO>> getParticipants(
            @PathVariable Long id,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var participants = eventService.getParticipantsByEvent(id, page, pageSize);
        return getApiResponseEntity(participants, ParticipantResponseDTO::fromEntity);
    }
}
