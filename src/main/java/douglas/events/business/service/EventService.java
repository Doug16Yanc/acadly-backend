package douglas.events.business.service;

import douglas.events.infraestructure.exception.local.AlreadyActiveEventException;
import douglas.events.infraestructure.exception.local.DateConflictException;
import douglas.events.infraestructure.exception.local.EventNotFoundException;
import douglas.events.infraestructure.exception.local.NotFoundException;
import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(Event event) {
        if (event.getFinalDate().isBefore(event.getInitialDate())) {
            throw new DateConflictException("A data de fim do evento não pode ser antes da sua data de início.");
        }

        var events = getAllEvents(0, Integer.MAX_VALUE).getContent();
        var hasActiveEvent = events.stream()
                .filter(Event::isActive)
                .toList();

        if (!(hasActiveEvent.isEmpty()) && event.isActive()) {
                throw new AlreadyActiveEventException("Só pode ter um evento ativo por vez.");
        }

        return eventRepository.save(event);
    }

    public Page<Event> getAllEvents(Integer page, Integer pageSize) {
        return eventRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Evento não encontrado.")
        );
    }

    public Event getActiveEvent() {
        var isActive = true;
        var event = eventRepository.findEventByIsActive(isActive);

        if (event == null) {
            throw new NotFoundException("Evento não encontrado.");
        }

        return event;
    }

    public Event getEventEntityById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));
    }

    public Event updateEvent(Long id, Event event) {
        var existentEvent = getEventEntityById(id);
        if (existentEvent == null) {
            throw new EventNotFoundException("Evento não encontrado com o ID: " + id);
        }
        if (event.getName() != null) {
            existentEvent.setName(event.getName());
        }
        if (event.getDescription() != null) {
            existentEvent.setDescription(event.getDescription());
        }
        if (event.getLocal() != null) {
            existentEvent.setLocal(event.getLocal());
        }
        if (event.getInitialDate() != null) {
            existentEvent.setInitialDate(event.getInitialDate());
        }
        if (event.getFinalDate() != null) {
            existentEvent.setFinalDate(event.getFinalDate());
        }
        return eventRepository.save(existentEvent);
    }
}
