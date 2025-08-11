package douglas.events.business.service;

import douglas.events.application.dto.EventDto;
import douglas.events.infraestructure.exception.local.AlreadyActiveEventException;
import douglas.events.infraestructure.exception.local.DateConflictException;
import douglas.events.infraestructure.exception.local.NotFoundException;
import douglas.events.infraestructure.exception.local.ListEmptyException;
import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(EventDto eventDto) {
        if (eventDto.finalDate().isBefore(eventDto.initialDate())) {
            throw new DateConflictException("A data de fim do evento não pode ser antes da sua data de início.");
        }

        var events = getAllEvents();

        var hasActiveEvent = events.stream()
                .filter(EventDto::isActive)
                .toList();

        if (!(hasActiveEvent.isEmpty())) {
            throw new AlreadyActiveEventException("Só pode ter um evento ativo por vez.");
        }

        var savedEvent = EventDto.toEntity(eventDto);

        return eventRepository.save(savedEvent);
    }

    public List<EventDto> getAllEvents() {
        var events = eventRepository.findAll();

        if (events.isEmpty()) {
            throw new ListEmptyException("Não há eventos salvos.");
        }

        return events.stream()
                .map(EventDto::fromEntity)
                .toList();
    }

    public EventDto getEventById(Long id) {
        var event = eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Evento não encontrado.")
        );

        return EventDto.fromEntity(event);
    }

    public EventDto getActiveEvent() {
        var isActive = true;
        var event = eventRepository.findEventByIsActive(isActive);

        if (event == null) {
            throw new NotFoundException("Evento não encontrado.");
        }

        return EventDto.fromEntity(event);
    }

    public Event getEventEntityById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));
    }
}
