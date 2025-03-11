package CourseManagerProject.CourseManager.service;

import CourseManagerProject.CourseManager.dto.EventDTO;
import CourseManagerProject.CourseManager.dto.EventRequest;
import CourseManagerProject.CourseManager.model.Classroom;
import CourseManagerProject.CourseManager.model.Event;
import CourseManagerProject.CourseManager.model.Tag;
import CourseManagerProject.CourseManager.model.User;
import CourseManagerProject.CourseManager.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Serwis odpowiedzialny za logikę biznesową związaną z wydarzeniami (encja {@link Event}).
 * <p>Oferuje operacje tworzenia, aktualizacji, usuwania oraz wyszukiwania wydarzeń
 * na podstawie różnych filtrów (organizer, sala, tagi, itp.).</p>
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ClassroomService classroomService;
    private final UserService userService;
    private final TagService tagService;

    /**
     * Konstruktor wstrzykujący zależności niezbędne do obsługi logiki kursów.
     *
     * @param eventRepository  Repozytorium do zapisu/odczytu encji {@link Event}.
     * @param classroomService Serwis odpowiadający za zarządzanie salami.
     * @param userService      Serwis do obsługi użytkowników, w tym organizatorów.
     * @param tagService       Serwis do obsługi tagów.
     */
    @Autowired
    public EventService(EventRepository eventRepository,
                        ClassroomService classroomService,
                        UserService userService,
                        TagService tagService) {
        this.eventRepository = eventRepository;
        this.classroomService = classroomService;
        this.userService = userService;
        this.tagService = tagService;
    }

    /**
     * Tworzy nowe wydarzenie na podstawie danych z obiektu {@link EventRequest}.
     * Sprawdza dostępność sali i weryfikuje rolę organizatora.
     *
     * @param eventRequest DTO z danymi wydarzenia.
     * @return Zapisany w bazie obiekt {@link Event}.
     * @throws IllegalArgumentException jeśli sala jest zajęta lub organizator nie ma uprawnień.
     */
    public Event createEvent(EventDTO eventRequest) {
        User organizer = userService.getOrganizer(eventRequest.getOrganizerId());
        Classroom classroom = classroomService.getClassroomById(eventRequest.getClassroomId());
        Set<Tag> tags = tagService.getTagsByIds(eventRequest.getTagIds());

        // Sprawdzenie dostępności sali
        // null bo ID tego eventu jeszcze nie istnieje
        if (!isClassroomAvailable(classroom.getId(), eventRequest.getStartDatetime(), eventRequest.getEndDatetime(), null)) {
            throw new IllegalArgumentException("Classroom not available at the given time");
        }

        Event event = new Event();
        event.setName(eventRequest.getName());
        event.setStartDatetime(eventRequest.getStartDatetime());
        event.setEndDatetime(eventRequest.getEndDatetime());
        event.setMaxParticipants(eventRequest.getMaxParticipants());
        event.setMinAge(eventRequest.getMinAge());
        event.setInfo(eventRequest.getInfo());
        event.setOrganizer(organizer);
        event.setClassroom(classroom);
        event.setTags(tags);

        return eventRepository.save(event);
    }

    /**
     * Aktualizuje istniejące wydarzenie na podstawie danych {@link EventRequest}.
     *
     * @param eventId      ID wydarzenia do aktualizacji.
     * @param eventRequest DTO z nowymi danymi wydarzenia.
     * @throws IllegalArgumentException jeśli wydarzenie nie istnieje, sala jest zajęta lub organizator nie ma uprawnień.
     */
    public void updateEvent(Integer eventId, EventDTO eventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        User organizer = userService.getOrganizer(eventRequest.getOrganizerId());
        Classroom classroom = classroomService.getClassroomById(eventRequest.getClassroomId());
        Set<Tag> tags = tagService.getTagsByIds(eventRequest.getTagIds());

        // Sprawdzanie dostępności sali z uwzględnieniem aktualnie edytowanego eventu
        if (!isClassroomAvailable(classroom.getId(), eventRequest.getStartDatetime(), eventRequest.getEndDatetime(), eventId)) {
            throw new IllegalArgumentException("Classroom not available at the given time");
        }

        event.setName(eventRequest.getName());
        event.setStartDatetime(eventRequest.getStartDatetime());
        event.setEndDatetime(eventRequest.getEndDatetime());
        event.setMaxParticipants(eventRequest.getMaxParticipants());
        event.setMinAge(eventRequest.getMinAge());
        event.setInfo(eventRequest.getInfo());
        event.setOrganizer(organizer);
        event.setClassroom(classroom);
        event.setTags(tags);

        eventRepository.save(event);
    }


    /**
     * Usuwa wydarzenie o podanym ID z bazy danych.
     *
     * @param eventId ID wydarzenia do usunięcia.
     * @throws IllegalArgumentException jeśli wydarzenie o podanym ID nie istnieje.
     */
    public void deleteEvent(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(event);
    }

    /**
     * Zwraca listę wydarzeń organizowanych przez określonego użytkownika (organizatora).
     *
     * @param organizerId ID organizatora.
     * @return Lista encji {@link Event} organizowanych przez danego użytkownika.
     */
    public List<Event> getOrganizedEvents(Integer organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    /**
     * Pobiera listę przeszłych wydarzeń (dla których data zakończenia jest mniejsza niż aktualny czas),
     * w których uczestniczy wskazany użytkownik.
     *
     * @param participantId ID uczestnika.
     * @return Lista encji {@link Event} z przeszłości.
     */
    public List<Event> getPastParticipatingEvents(Integer participantId) {
        return eventRepository.findPastEventsByParticipantId(participantId, LocalDateTime.now());
    }

    /**
     * Pobiera listę przyszłych wydarzeń (dla których data rozpoczęcia jest większa niż aktualny czas),
     * w których uczestniczy wskazany użytkownik.
     *
     * @param participantId ID uczestnika.
     * @return Lista encji {@link Event} z przyszłości.
     */
    public List<Event> getFutureParticipatingEvents(Integer participantId) {
        return eventRepository.findFutureEventsByParticipantId(participantId, LocalDateTime.now());
    }

    /**
     * Przeszukuje dostępne wydarzenia na podstawie filtrów (organizerId, classroomId, tagId, excludeFull).
     *
     * @param organizerId  (opcjonalne) ID organizatora.
     * @param classroomId  (opcjonalne) ID sali.
     * @param tagId        (opcjonalne) ID tagu.
     * @param excludeFull  (opcjonalne) Czy wykluczać pełne wydarzenia.
     * @return Lista encji {@link Event} spełniających podane kryteria.
     */
    public List<Event> searchEvents(Integer organizerId, Integer classroomId, Integer tagId, boolean excludeFull) {
        return eventRepository.findAvailableEvents(LocalDateTime.now(), organizerId, classroomId, tagId, excludeFull);
    }

    /**
     * Sprawdza, czy sala jest wolna w zadanym przedziale czasowym.
     *
     * @param classroomId ID sali.
     * @param start       Data i czas rozpoczęcia.
     * @param end         Data i czas zakończenia.
     * @return true, jeśli sala nie jest zajęta w danym przedziale czasowym; false w przeciwnym razie.
     */
//    private boolean isClassroomAvailable(Integer classroomId, LocalDateTime start, LocalDateTime end) {
//        List<Event> overlapping = eventRepository.findByClassroomIdAndStartDatetimeBeforeAndEndDatetimeAfter(
//                classroomId, end, start);
//        return overlapping.isEmpty();
//    }

    // drobna modyfikacja, przyjmuuje event_id, aby nie dawało erroru o overlapie sal podczas edycji eventu
    private boolean isClassroomAvailable(Integer classroomId, LocalDateTime start, LocalDateTime end, Integer eventId) {
        List<Event> overlapping = eventRepository.findByClassroomIdAndStartDatetimeBeforeAndEndDatetimeAfter(
                classroomId, end, start);

        // Ignorujemy konflikt z aktualnie edytowanym eventem, jeśli eventId nie jest null
        return overlapping.stream().noneMatch(event -> eventId != null && !event.getId().equals(eventId));
    }




    /**
     * Mapuje encję Event na DTO.
     *
     * @param event Encja Event.
     * @return Obiekt EventDTO.
     */
    public EventDTO mapToDTO(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .startDatetime(event.getStartDatetime())
                .endDatetime(event.getEndDatetime())
                .maxParticipants(event.getMaxParticipants())
                .minAge(event.getMinAge())
                .info(event.getInfo())
                .organizerId(event.getOrganizer().getId())
                .organizerName(event.getOrganizer().getFirstname() + " " + event.getOrganizer().getSurname())
                .classroomId(event.getClassroom().getId())
                .classroomName(event.getClassroom().getClassroomName())
                .tagIds(event.getTags().stream().map(tag -> tag.getId()).toList())
                .build();
    }


    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

}
