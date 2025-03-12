package CourseManagerProject.CourseManager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Klasa reprezentująca tag kursu w systemie zarządzania kursami na uczelni.
 *
 * <p>Encja {@code Tag} służy do kategoryzowania wydarzeń (kursów) poprzez przypisanie im określonych
 * etykiet tematycznych, takich jak "Matematyka", "Rachunek różniczkowy", "Informatyka" itp.
 *
 * <p>Relacje:
 * <ul>
 *     <li>{@code ManyToMany} z {@code Event} - tag może być przypisany do wielu wydarzeń, a wydarzenie może posiadać wiele tagów.</li>
 * </ul>
 *
 * <p>Walidacje:
 * <ul>
 *     <li>{@code name} jest obowiązkowe, unikalne oraz ma maksymalną długość 50 znaków.</li>
 * </ul>
 *
 * <p>Adnotacje Lombok {@code @Data}, {@code @NoArgsConstructor}, {@code @AllArgsConstructor} oraz {@code @Builder}
 * automatycznie generują gettery, settery, konstruktory bezargumentowy i pełny oraz wzorzec builder dla tej klasy.
 *
 * <p>Adnotacje JPA:
 * <ul>
 *     <li>{@code @Entity} - oznacza klasę jako encję JPA.</li>
 *     <li>{@code @Table(name = "tags")} - określa nazwę tabeli w bazie danych.</li>
 *     <li>{@code @Id} oraz {@code @GeneratedValue} - definiują klucz główny i strategię jego generowania.</li>
 *     <li>{@code @Column(unique = true)} - zapewnia unikalność kolumny {@code name} w tabeli.</li>
 *     <li>{@code @ManyToMany(mappedBy = "tags")} - definiuje relację wiele-do-wielu z encją {@code Event}, gdzie tag jest przypisany do wielu wydarzeń.</li>
 * </ul>
 *
 * <p>Przykładowe zastosowanie klasy {@code Tag} w kontekście aplikacji:
 * <pre>{@code
 * // Tworzenie nowego tagu za pomocą wzorca Builder
 * Tag tag = Tag.builder()
 *     .name("Matematyka")
 *     .build();
 *
 * // Przypisywanie tagu do wydarzenia
 * Event event = new Event();
 * event.getTags().add(tag);
 * tag.getEvents().add(event);
 * }</pre>
 *
 */
@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag {

    /**
     * Unikalny identyfikator tagu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    /**
     * Nazwa tagu. Musi być unikalna i nie może być pusta.
     */
    @NotBlank(message = "Nazwa tagu nie może być pusta")
    @Size(max = 50, message = "Nazwa tagu nie może przekraczać 50 znaków")
    @Column(unique = true)
    private String name;

    /**
     * Zestaw wydarzeń, które są oznaczone tym tagiem.
     * <p>Relacja wiele-do-wielu z encją {@code Event}.
     */
    @Builder.Default
    @ToString.Exclude
    @ManyToMany(mappedBy = "tags")
    @JsonBackReference // Pomija serializację w odwrotnej relacji z Event
    private Set<Event> events = new HashSet<>();
}
