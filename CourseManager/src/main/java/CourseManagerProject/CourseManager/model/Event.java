package CourseManagerProject.CourseManager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Klasa reprezentująca wydarzenie (kurs) w systemie zarządzania kursami na uczelni.
 *
 * <p>Encja {@code Event} zawiera informacje o konkretnym kursie, w tym jego nazwę, czas trwania,
 * maksymalną liczbę uczestników, wymagany minimalny wiek oraz dodatkowe informacje. Wydarzenie
 * jest przypisane do konkretnej sali oraz organizowane przez użytkownika (organizer).
 *
 * <p>Relacje:
 * <ul>
 *     <li>{@code ManyToOne} z {@code User} - wydarzenie jest organizowane przez jednego użytkownika.</li>
 *     <li>{@code ManyToOne} z {@code Classroom} - wydarzenie odbywa się w jednej sali.</li>
 *     <li>{@code ManyToMany} z {@code User} - wielu użytkowników może brać udział w wydarzeniu jako uczestnicy.</li>
 *     <li>{@code ManyToMany} z {@code Tag} - wydarzenie może posiadać wiele tagów tematycznych.</li>
 * </ul>
 *
 * <p>Walidacje:
 * <ul>
 *     <li>{@code name} jest obowiązkowe i ma maksymalną długość 100 znaków.</li>
 *     <li>{@code startDatetime} i {@code endDatetime} są obowiązkowe.</li>
 *     <li>{@code maxParticipants} jest obowiązkowe i musi być co najmniej 1.</li>
 *     <li>{@code minAge} jest obowiązkowe i nie może być ujemne.</li>
 * </ul>
 *
 * <p>Adnotacje Lombok {@code @Data}, {@code @NoArgsConstructor}, {@code @AllArgsConstructor} oraz {@code @Builder}
 * automatycznie generują gettery, settery, konstruktory bezargumentowy i pełny oraz wzorzec builder dla tej klasy.
 *
 * <p>Adnotacje JPA:
 * <ul>
 *     <li>{@code @Entity} - oznacza klasę jako encję JPA.</li>
 *     <li>{@code @Table(name = "events")} - określa nazwę tabeli w bazie danych.</li>
 *     <li>{@code @Id} oraz {@code @GeneratedValue} - definiują klucz główny i strategię jego generowania.</li>
 *     <li>{@code @ManyToOne} - definiuje relację wiele-do-jednego z encjami {@code User} i {@code Classroom}.</li>
 *     <li>{@code @ManyToMany} - definiuje relacje wiele-do-wielu z encjami {@code User} (uczestnicy) i {@code Tag}.</li>
 * </ul>
 *
 * <p>Przykładowe zastosowanie klasy {@code Event} w kontekście aplikacji:
 * <pre>{@code
 * // Tworzenie nowego wydarzenia za pomocą wzorca Builder
 * Event event = Event.builder()
 *     .name("Kurs Programowania")
 *     .startDatetime(LocalDateTime.of(2024, 5, 20, 10, 0))
 *     .endDatetime(LocalDateTime.of(2024, 5, 20, 12, 0))
 *     .maxParticipants(30)
 *     .minAge(18)
 *     .info("Wprowadzenie do programowania w Javie.")
 *     .organizer(user)
 *     .classroom(classroom)
 *     .build();
 *
 * // Dodawanie uczestnika do wydarzenia
 * event.getParticipants().add(user);
 * user.getParticipatingEvents().add(event);
 *
 * // Przypisywanie tagu do wydarzenia
 * Tag tag = Tag.builder().name("Informatyka").build();
 * event.getTags().add(tag);
 * tag.getEvents().add(event);
 * }</pre>
 *
 */
@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {

    /**
     * Unikalny identyfikator wydarzenia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    /**
     * Nazwa wydarzenia. Musi być niepusta i nie może przekraczać 100 znaków.
     */
    @NotBlank(message = "Nazwa wydarzenia nie może być pusta")
    @Size(max = 100, message = "Nazwa wydarzenia nie może przekraczać 100 znaków")
    private String name;

    /**
     * Data i czas rozpoczęcia wydarzenia. Pole jest obowiązkowe.
     */
    @NotNull(message = "Data i czas rozpoczęcia wydarzenia są obowiązkowe")
    private LocalDateTime startDatetime;

    /**
     * Data i czas zakończenia wydarzenia. Pole jest obowiązkowe.
     */
    @NotNull(message = "Data i czas zakończenia wydarzenia są obowiązkowe")
    private LocalDateTime endDatetime;

    /**
     * Maksymalna liczba uczestników wydarzenia. Musi być co najmniej 1.
     */
    @NotNull(message = "Maksymalna liczba uczestników jest obowiązkowa")
    @Min(value = 1, message = "Maksymalna liczba uczestników musi być co najmniej 1")
    private Integer maxParticipants;

    /**
     * Minimalny wiek wymagany do udziału w wydarzeniu. Nie może być ujemny.
     */
    @NotNull(message = "Minimalny wiek jest obowiązkowy")
    @Min(value = 0, message = "Minimalny wiek nie może być ujemny")
    private Integer minAge;

    /**
     * Dodatkowe informacje o wydarzeniu.
     */
    private String info;

    /**
     * Użytkownik (organizer) odpowiedzialny za organizację wydarzenia.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    @JsonManagedReference // Kontroluje serializację w relacji z User
    private User organizer;

    /**
     * Sala, w której odbywa się wydarzenie.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    @JsonManagedReference // Kontroluje serializację w relacji z Classroom
    private Classroom classroom;

    /**
     * Zestaw użytkowników uczestniczących w wydarzeniu.
     */
    @Builder.Default
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "event_participant",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties("participatingEvents") // Ignoruje odwrotne pole w User
    private Set<User> participants = new HashSet<>();

    /**
     * Zestaw tagów tematycznych przypisanych do wydarzenia.
     */
    @Builder.Default
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "event_tag",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonIgnoreProperties("events") // Ignoruje odwrotne pole w Tag
    private Set<Tag> tags = new HashSet<>();
}
