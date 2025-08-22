package douglas.events.business.service;

import douglas.events.infraestructure.exception.local.AlreadyActiveEventException;
import douglas.events.infraestructure.exception.local.DateConflictException;
import douglas.events.infraestructure.exception.local.EventNotFoundException;
import douglas.events.infraestructure.exception.local.NotFoundException;
import douglas.events.infraestructure.model.Event;
import douglas.events.infraestructure.model.Person;
import douglas.events.infraestructure.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public Event createEvent(Event event) {
        if (event.getFinalDateTime().isBefore(event.getInitialDateTime())) {
            throw new DateConflictException("A data de fim do evento não pode ser antes da sua data de início.");
        }

        var latestEventOpt = eventRepository.findTopByOrderByFinalDateTimeDesc();

        if (latestEventOpt.isPresent()) {
            Event latestEvent = latestEventOpt.get();

            if (!event.getInitialDateTime().isAfter(latestEvent.getFinalDateTime())) {
                throw new DateConflictException(
                        "A data e hora de início do novo evento (" + event.getInitialDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss")) +
                                ") deve ser posterior à data e hora de término do último evento já agendado (" + latestEvent.getFinalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss")) + ")."
                );
            }
        }

        if (event.getIsActive()) {
            var activeEventOpt = eventRepository.findTopByIsActiveTrueOrderByFinalDateTimeDesc();

            if (activeEventOpt.isPresent()) {
                throw new AlreadyActiveEventException(
                        "Não é possível criar um novo evento ativo, pois o evento '" + activeEventOpt.get().getName() + "' já está ativo."
                );
            }
        }

        return eventRepository.save(event);
    }

    public Page<Event> getAllEvents(String query, Integer page, Integer pageSize) {
        var pageable = PageRequest.of(page, pageSize, Sort.by("initialDateTime").descending());

        if (query != null && !query.trim().isEmpty()) {
            return eventRepository.findByNameContainingIgnoreCase(query, pageable);
        }

        return eventRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Evento não encontrado.")
        );
    }

    public Page<Person> getParticipantsByEvent(Long eventId, Integer page, Integer pageSize) {
        eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Evento naão encontrado"));

        return eventRepository.findAllParticipantsByEvent(eventId, PageRequest.of(page, pageSize));
    }

    public boolean existsActive() {
        return eventRepository.existsByIsActiveTrue();
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

    @Transactional
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
        if (event.getInitialDateTime() != null) {
            existentEvent.setInitialDateTime(event.getInitialDateTime());
        }
        if (event.getFinalDateTime() != null) {
            existentEvent.setFinalDateTime(event.getFinalDateTime());
        }
        if (event.getIsActive() != existentEvent.getIsActive()) {
            existentEvent.setIsActive(event.getIsActive());
        }
        return eventRepository.save(existentEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        var existentEvent = getEventEntityById(id);
        if (existentEvent == null) {
            throw new EventNotFoundException("Evento não encontrado com o ID: " + id);
        }
        eventRepository.deleteById(id);
    }
}
