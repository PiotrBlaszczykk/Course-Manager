package CourseManagerProject.CourseManager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Klasa reprezentująca użytkownika systemu zarządzania kursami na uczelni.
 *
 * <p>Encja {@code User} zawiera informacje o uczestnikach kursów oraz organizatorach.
 * Użytkownicy mogą pełnić rolę uczestników, organizatorów lub obu jednocześnie,
 * co jest określane przez pole {@code isOrganizer}.
 *
 * <p>Relacje:
 * <ul>
 *     <li>{@code OneToMany} z {@code Event} - użytkownik może być organizatorem wielu wydarzeń.</li>
 *     <li>{@code ManyToMany} z {@code Event} - użytkownik może brać udział w wielu wydarzeniach jako uczestnik.</li>
 * </ul>
 *
 * <p>Walidacje:
 * <ul>
 *     <li>{@code firstname} i {@code surname} są obowiązkowe i mają maksymalną długość 50 znaków.</li>
 *     <li>{@code email} jest obowiązkowy, unikalny, poprawnego formatu oraz ma maksymalną długość 100 znaków.</li>
 *     <li>{@code password} jest obowiązkowy, ma minimalną długość 8 znaków i maksymalną długość 255 znaków.</li>
 *     <li>{@code age} musi być liczbą nieujemną.</li>
 *     <li>{@code isOrganizer} jest obowiązkowym polem logicznym.</li>
 * </ul>
 *
 * <p>Adnotacje Lombok {@code @Data}, {@code @NoArgsConstructor}, {@code @AllArgsConstructor} oraz {@code @Builder}
 * automatycznie generują gettery, settery, konstruktory bezargumentowy i pełny oraz wzorzec builder dla tej klasy.
 *
 * <p>Adnotacje JPA:
 * <ul>
 *     <li>{@code @Entity} - oznacza klasę jako encję JPA.</li>
 *     <li>{@code @Table(name = "users")} - określa nazwę tabeli w bazie danych.</li>
 *     <li>{@code @Id} oraz {@code @GeneratedValue} - definiują klucz główny i strategię jego generowania.</li>
 *     <li>{@code @Column(unique = true)} - zapewnia unikalność kolumny {@code email} w tabeli.</li>
 *     <li>{@code @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)} -
 *         definiuje relację jeden-do-wielu z encją {@code Event}, gdzie użytkownik jest organizatorem.</li>
 *     <li>{@code @ManyToMany(mappedBy = "participants")} - definiuje relację wiele-do-wielu z encją {@code Event},
 *         gdzie użytkownik jest uczestnikiem.</li>
 * </ul>
 *
 */
@Entity
@Table(name = "users") // Unikamy nazwy "User" jako nazwy tabeli, ponieważ jest to słowo kluczowe w niektórych bazach danych
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    /**
     * Unikalny identyfikator użytkownika.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    /**
     * Imię użytkownika.
     */
    @NotBlank(message = "Imię nie może być puste")
    @Size(max = 50, message = "Imię nie może przekraczać 50 znaków")
    private String firstname;

    /**
     * Nazwisko użytkownika.
     */
    @NotBlank(message = "Nazwisko nie może być puste")
    @Size(max = 50, message = "Nazwisko nie może przekraczać 50 znaków")
    private String surname;

    /**
     * Wiek użytkownika.
     */
    @Min(value = 0, message = "Wiek nie może być ujemny")
    private Integer age;

    /**
     * Adres e-mail użytkownika. Musi być unikalny i poprawnego formatu.
     */
    @Email(message = "Email powinien być poprawny")
    @NotBlank(message = "Email nie może być pusty")
    @Size(max = 100, message = "Email nie może przekraczać 100 znaków")
    @Column(unique = true)
    private String email;

    /**
     * Hasło użytkownika. Minimalna długość to 8 znaków.
     * <p>Hasła powinny być przechowywane w postaci zaszyfrowanej.
     */
    @NotBlank(message = "Hasło nie może być puste")
    @Size(min = 8, max = 255, message = "Hasło musi mieć od 8 do 255 znaków")
    private String password;

    /**
     * Flaga wskazująca, czy użytkownik jest organizatorem.
     */
    @NotNull(message = "Pole isOrganizer nie może być puste")
    private Boolean isOrganizer;

    /**
     * Zestaw wydarzeń, które użytkownik organizuje.
     * <p>Relacja jeden-do-wielu z encją {@code Event}.
     */
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "organizedEvents") // Pomija serializację odwrotnej relacji z Event
    private Set<Event> organizedEvents = new HashSet<>();

    /**
     * Zestaw wydarzeń, w których użytkownik bierze udział jako uczestnik.
     * <p>Relacja wiele-do-wielu z encją {@code Event}.
     */
    @Builder.Default
    @ToString.Exclude
    @ManyToMany(mappedBy = "participants")
    @JsonBackReference(value = "participatingEvents") // Pomija serializację odwrotnej relacji z Event
    private Set<Event> participatingEvents = new HashSet<>();
}
