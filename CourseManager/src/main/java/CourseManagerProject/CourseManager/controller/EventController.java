package CourseManagerProject.CourseManager.controller;

import CourseManagerProject.CourseManager.dto.EventDTO;
import CourseManagerProject.CourseManager.model.Event;
import CourseManagerProject.CourseManager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler REST odpowiedzialny za zarządzanie wydarzeniami (kursami).
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Tworzy nowe wydarzenie na podstawie danych DTO.
     *
     * @param eventRequest Obiekt {@link EventDTO} z danymi kursu.
     * @return Komunikat zawierający ID utworzonego wydarzenia.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@Validated @RequestBody EventDTO eventRequest) {
        Event createdEvent = eventService.createEvent(eventRequest);
        return ResponseEntity.ok("Event created with ID: " + createdEvent.getId());
    }

    /**
     * Aktualizuje istniejące wydarzenie na podstawie danych DTO.
     */
    @PutMapping("/{eventId}/update")
    public ResponseEntity<String> updateEvent(
            @PathVariable Integer eventId,
            @Validated @RequestBody EventDTO eventRequest
    ) {
        eventService.updateEvent(eventId, eventRequest);
        return ResponseEntity.ok("Event with ID " + eventId + " has been updated successfully.");
    }

    /**
     * Usuwa wydarzenie o podanym ID.
     */
    @DeleteMapping("/{eventId}/delete")
    public ResponseEntity<String> deleteEvent(@PathVariable Integer eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok("Event with ID " + eventId + " has been deleted successfully.");
    }

    /**
     * Pobiera listę wydarzeń organizowanych przez konkretnego organizatora.
     *
     * @param organizerId ID organizatora (użytkownika).
     * @return Lista obiektów {@link EventDTO} prowadzonych przez danego organizatora.
     */
    @GetMapping("/organizers/{organizerId}/events")
    public ResponseEntity<List<EventDTO>> getOrganizedEvents(@PathVariable Integer organizerId) {
        List<Event> events = eventService.getOrganizedEvents(organizerId);
        List<EventDTO> eventDTOs = events.stream()
                .map(eventService::mapToDTO)
                .toList();
        return ResponseEntity.ok(eventDTOs);
    }

    /**
     * Pobiera listę przeszłych wydarzeń, w których uczestniczył użytkownik.
     *
     * @param participantId ID uczestnika (użytkownika).
     * @return Lista przeszłych obiektów {@link EventDTO}.
     */
    @GetMapping("/participants/{participantId}/past")
    public ResponseEntity<List<EventDTO>> getPastParticipatingEvents(@PathVariable Integer participantId) {
        List<Event> pastEvents = eventService.getPastParticipatingEvents(participantId);
        List<EventDTO> eventDTOs = pastEvents.stream()
                .map(eventService::mapToDTO)
                .toList();
        return ResponseEntity.ok(eventDTOs);
    }

    /**
     * Pobiera listę przyszłych wydarzeń, w których uczestniczy użytkownik.
     *
     * @param participantId ID uczestnika (użytkownika).
     * @return Lista przyszłych obiektów {@link EventDTO}.
     */
    @GetMapping("/participants/{participantId}/future")
    public ResponseEntity<List<EventDTO>> getFutureParticipatingEvents(@PathVariable Integer participantId) {
        List<Event> futureEvents = eventService.getFutureParticipatingEvents(participantId);
        List<EventDTO> eventDTOs = futureEvents.stream()
                .map(eventService::mapToDTO)
                .toList();
        return ResponseEntity.ok(eventDTOs);
    }

    /**
     * Pozwala na filtrowanie dostępnych wydarzeń według organizatora, sali,
     * tagu oraz parametru wykluczania pełnych kursów.
     *
     * @param organizerId  (opcjonalne) ID organizatora.
     * @param classroomId  (opcjonalne) ID sali.
     * @param tagId        (opcjonalne) ID tagu.
     * @param excludeFull  (opcjonalne) Czy wykluczyć wydarzenia pełne. Domyślnie false.
     * @return Lista wydarzeń spełniających zadane kryteria.
     */
    @GetMapping("/filtered")
    public ResponseEntity<List<EventDTO>> getFilteredEvents(
            @RequestParam(required = false) Integer organizerId,
            @RequestParam(required = false) Integer classroomId,
            @RequestParam(required = false) Integer tagId,
            @RequestParam(defaultValue = "false") boolean excludeFull
    ) {
        List<Event> filteredEvents = eventService.searchEvents(organizerId, classroomId, tagId, excludeFull);
        List<EventDTO> eventDTOs = filteredEvents.stream()
                .map(eventService::mapToDTO)
                .toList();
        return ResponseEntity.ok(eventDTOs);
    }

    /**
     * Pobiera listę wszystkich wydarzeń.
     *
     * @return Lista obiektów {@link EventDTO}.
     */
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<Event> allEvents = eventService.findAllEvents();
        List<EventDTO> eventDTOs = allEvents.stream()
                .map(eventService::mapToDTO)
                .toList();
        return ResponseEntity.ok(eventDTOs);
    }
}
