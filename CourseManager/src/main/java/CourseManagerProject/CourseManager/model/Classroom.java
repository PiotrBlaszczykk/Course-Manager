package CourseManagerProject.CourseManager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Klasa reprezentująca salę wykładową w systemie zarządzania kursami na uczelni.
 *
 * <p>Encja {@code Classroom} zawiera informacje o dostępnych salach, takich jak ich pojemność,
 * lokalizacja, dodatkowe informacje oraz unikalna nazwa. Sala może być przypisana do wielu
 * wydarzeń (kursów), które odbywają się w niej.
 *
 * <p>Relacje:
 * <ul>
 *     <li>{@code OneToMany} z {@code Event} - sala może być przypisana do wielu wydarzeń.</li>
 * </ul>
 *
 * <p>Walidacje:
 * <ul>
 *     <li>{@code capacity} jest obowiązkowe i musi być co najmniej 1.</li>
 *     <li>{@code location} jest obowiązkowe i ma maksymalną długość 100 znaków.</li>
 *     <li>{@code classroomName} jest obowiązkowe, unikalne oraz ma maksymalną długość 100 znaków.</li>
 * </ul>
 *
 * <p>Adnotacje Lombok {@code @Data}, {@code @NoArgsConstructor}, {@code @AllArgsConstructor} oraz {@code @Builder}
 * automatycznie generują gettery, settery, konstruktory bezargumentowy i pełny oraz wzorzec builder dla tej klasy.
 *
 * <p>Adnotacje JPA:
 * <ul>
 *     <li>{@code @Entity} - oznacza klasę jako encję JPA.</li>
 *     <li>{@code @Table(name = "classrooms")} - określa nazwę tabeli w bazie danych.</li>
 *     <li>{@code @Id} oraz {@code @GeneratedValue} - definiują klucz główny i strategię jego generowania.</li>
 *     <li>{@code @Column(unique = true)} - zapewnia unikalność kolumny {@code classroomName} w tabeli.</li>
 *     <li>{@code @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)} -
 *         definiuje relację jeden-do-wielu z encją {@code Event}, gdzie sala jest przypisana do wielu wydarzeń.</li>
 * </ul>
 *
 * <p>Przykładowe zastosowanie klasy {@code Classroom} w kontekście aplikacji:
 * <pre>{@code
 * // Tworzenie nowej sali za pomocą wzorca Builder
 * Classroom classroom = Classroom.builder()
 *     .capacity(50)
 *     .location("Budynek A, Sala 101")
 *     .info("Sala wyposażona w projektor i system audio.")
 *     .classroomName("Sala 101")
 *     .build();
 *
 * // Przypisywanie wydarzenia do sali
 * Event event = new Event();
 * event.setClassroom(classroom);
 * classroom.getEvents().add(event);
 * }</pre>
 *
 */
@Entity
@Table(name = "classrooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Classroom {

    /**
     * Unikalny identyfikator sali.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    /**
     * Pojemność sali. Musi być co najmniej 1.
     */
    @NotNull(message = "Pojemność sali jest obowiązkowa")
    @Min(value = 1, message = "Pojemność sali musi być co najmniej 1")
    private Integer capacity;

    /**
     * Lokalizacja sali. Musi być niepusta i nie może przekraczać 100 znaków.
     */
    @NotBlank(message = "Lokalizacja sali nie może być pusta")
    @Size(max = 100, message = "Lokalizacja sali nie może przekraczać 100 znaków")
    private String location;

    /**
     * Dodatkowe informacje o sali.
     */
    private String info;

    /**
     * Unikalna nazwa sali. Musi być niepusta, unikalna i nie może przekraczać 100 znaków.
     */
    @NotBlank(message = "Nazwa sali nie może być pusta")
    @Size(max = 100, message = "Nazwa sali nie może przekraczać 100 znaków")
    @Column(unique = true)
    private String classroomName;

    /**
     * Zestaw wydarzeń, które odbywają się w tej sali.
     */
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // Pomija serializację w odwrotnej relacji z Event
    private Set<Event> events = new HashSet<>();
}
