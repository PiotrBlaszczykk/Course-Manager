package CourseManagerProject.CourseManager.repository;

import CourseManagerProject.CourseManager.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repozytorium Spring Data JPA do operacji CRUD na encjach {@link Event}.
 * <p>Zawiera niestandardowe metody wyszukiwania wydarzeń według uczestników,
 * organizatorów, tagów oraz dostępności sali.</p>
 */
public interface EventRepository extends JpaRepository<Event, Integer> {

    /**
     * Zwraca listę wydarzeń organizowanych przez użytkownika o podanym ID.
     *
     * @param organizerId ID organizatora.
     * @return Lista encji {@link Event} prowadzonych przez danego użytkownika.
     */
    List<Event> findByOrganizerId(Integer organizerId);

    /**
     * Zwraca listę wydarzeń, w których uczestniczy użytkownik o podanym ID.
     *
     * @param participantId ID uczestnika (użytkownika).
     * @return Lista encji {@link Event}, w których uczestniczy dany użytkownik.
     */
    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p.id = :participantId")
    List<Event> findByParticipantId(@Param("participantId") Integer participantId);

    /**
     * Zwraca listę przeszłych wydarzeń (o endDatetime < now), w których
     * uczestniczy użytkownik o podanym ID.
     *
     * @param participantId ID uczestnika.
     * @param now           Aktualny czas (np. LocalDateTime.now()).
     * @return Lista encji {@link Event} z przeszłości.
     */
    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p.id = :participantId AND e.endDatetime < :now")
    List<Event> findPastEventsByParticipantId(@Param("participantId") Integer participantId,
                                              @Param("now") LocalDateTime now);

    /**
     * Zwraca listę przyszłych wydarzeń (o startDatetime > now), w których
     * uczestniczy użytkownik o podanym ID.
     *
     * @param participantId ID uczestnika.
     * @param now           Aktualny czas (np. LocalDateTime.now()).
     * @return Lista encji {@link Event} z przyszłości.
     */
    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p.id = :participantId AND e.startDatetime > :now")
    List<Event> findFutureEventsByParticipantId(@Param("participantId") Integer participantId,
                                                @Param("now") LocalDateTime now);

    /**
     * Sprawdza, czy sala o podanym ID jest wolna w zadanym zakresie czasu
     * (od start do end). Zwraca listę kursów, które kolidują z tym czasem.
     *
     * @param classroomId ID sali.
     * @param end         Data i czas końca.
     * @param start       Data i czas początku.
     * @return Lista encji {@link Event} kolidujących z podanym przedziałem czasu.
     */
    List<Event> findByClassroomIdAndStartDatetimeBeforeAndEndDatetimeAfter(Integer classroomId,
                                                                           LocalDateTime end,
                                                                           LocalDateTime start);

    /**
     * Wyszukuje wydarzenia dostępne według określonych filtrów, takich jak:
     * organizator, sala, tag oraz wykluczanie pełnych wydarzeń.
     *
     * @param now         Aktualny czas.
     * @param organizerId (opcjonalne) ID organizatora.
     * @param classroomId (opcjonalne) ID sali.
     * @param tagId       (opcjonalne) ID tagu.
     * @param excludeFull (opcjonalne) Czy wykluczać wydarzenia pełne.
     * @return Lista encji {@link Event} spełniających zadane kryteria wyszukiwania.
     */
    @Query("SELECT e FROM Event e " +
            "WHERE (:now IS NULL OR e.startDatetime >= :now) " +
            "AND (:organizerId IS NULL OR e.organizer.id = :organizerId) " +
            "AND (:classroomId IS NULL OR e.classroom.id = :classroomId) " +
            "AND (:tagId IS NULL OR EXISTS (SELECT t FROM e.tags t WHERE t.id = :tagId)) " +
            "AND (:excludeFull = false OR SIZE(e.participants) < e.maxParticipants)")
    List<Event> findAvailableEvents(@Param("now") LocalDateTime now,
                                    @Param("organizerId") Integer organizerId,
                                    @Param("classroomId") Integer classroomId,
                                    @Param("tagId") Integer tagId,
                                    @Param("excludeFull") boolean excludeFull);
}
