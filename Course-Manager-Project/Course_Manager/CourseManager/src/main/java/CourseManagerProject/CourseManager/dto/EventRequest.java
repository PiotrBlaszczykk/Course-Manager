package CourseManagerProject.CourseManager.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO (Data Transfer Object) służący do przesyłania danych związanych z tworzeniem
 * lub aktualizacją obiektu typu {@link CourseManagerProject.CourseManager.model.Event}.
 * <p>Zawiera informacje takie jak nazwa wydarzenia, daty rozpoczęcia i zakończenia,
 * liczba uczestników, wiek minimalny, ID sali, ID organizatora oraz ID tagów.</p>
 */
@Setter
@Getter
public class EventRequest {

    /**
     * Nazwa wydarzenia (np. "Kurs Matematyki Dyskretnej").
     * Pole wymagane, nie może być puste.
     */
    @NotBlank(message = "Nazwa wydarzenia jest wymagana")
    private String name;

    /**
     * Data i czas rozpoczęcia wydarzenia.
     * Musi być wartością przyszłą (adnotacja @Future).
     */
    @NotNull(message = "Data i czas rozpoczęcia są wymagane")
    @Future(message = "Data rozpoczęcia musi być w przyszłości")
    private LocalDateTime startDatetime;

    /**
     * Data i czas zakończenia wydarzenia.
     * Musi być wartością przyszłą (adnotacja @Future).
     */
    @NotNull(message = "Data i czas zakończenia są wymagane")
    @Future(message = "Data zakończenia musi być w przyszłości")
    private LocalDateTime endDatetime;

    /**
     * Maksymalna liczba uczestników wydarzenia.
     * Wymagane, musi być co najmniej 1.
     */
    @NotNull(message = "Maksymalna liczba uczestników jest wymagana")
    @Min(value = 1, message = "Maksymalna liczba uczestników musi być przynajmniej 1")
    private Integer maxParticipants;

    /**
     * Minimalny wiek uczestników.
     * Musi być liczbą nieujemną.
     */
    @NotNull(message = "Minimalny wiek jest wymagany")
    @Min(value = 0, message = "Minimalny wiek nie może być ujemny")
    private Integer minAge;

    /**
     * Dodatkowe informacje o wydarzeniu (np. "Wymagane podstawy z rachunku różniczkowego").
     * Może być wartościom pustą.
     */
    private String info;

    /**
     * Identyfikator sali (Classroom), w której odbędzie się wydarzenie.
     */
    @NotNull(message = "ID sali jest wymagane")
    private Integer classroomId;

    /**
     * Lista identyfikatorów tagów przypisanych do wydarzenia (np. "Matematyka", "Nauki Przyrodnicze").
     */
    private List<@NotNull Integer> tagIds;

    /**
     * Identyfikator organizatora (User), który prowadzi wydarzenie.
     */
    @NotNull(message = "ID organizatora jest wymagane")
    private Integer organizerId;

    // Gettery i Settery

}
