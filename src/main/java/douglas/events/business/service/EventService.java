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

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(Event event) {
        if (event.getFinalDate().isBefore(event.getInitialDate())) {
            throw new DateConflictException("A data de fim do evento não pode ser antes da sua data de início.");
        }

        var latestEventOpt = eventRepository.findTopByOrderByFinalDateDesc();

        if (latestEventOpt.isPresent()) {
            Event latestEvent = latestEventOpt.get();

            if (!event.getInitialDate().isAfter(latestEvent.getFinalDate())) {
                throw new DateConflictException(
                        "A data de início do novo evento (" + event.getInitialDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                                ") deve ser posterior à data de término do último evento já agendado (" + latestEvent.getFinalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")."
                );
            }
        }

        if (event.isActive()) {
            var activeEventOpt = eventRepository.findTopByIsActiveTrueOrderByFinalDateDesc();

            if (activeEventOpt.isPresent()) {
                throw new AlreadyActiveEventException(
                        "Não é possível criar um novo evento ativo, pois o evento '" + activeEventOpt.get().getName() + "' já está ativo."
                );
            }
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
