package CourseManagerProject.CourseManager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Obiekt DTO (Data Transfer Object) służący do przesyłania danych
 * potrzebnych do tworzenia lub aktualizacji obiektu typu {@link Classroom}.
 * <p>Zawiera minimalne informacje, takie jak pojemność sali, jej lokalizację
 * oraz nazwę, a także dodatkowe pola informacyjne.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomDTO {

    /**
     * Pojemność sali. Wymagane jest, by miała co najmniej 1 miejsce.
     */
    @NotNull
    @Min(1)
    private Integer capacity;

    /**
     * Lokalizacja sali (np. budynek, kampus).
     */
    @NotBlank
    private String location;

    /**
     * Dodatkowe informacje o sali.
     * Może być to opis wyposażenia lub ostrzeżenia.
     */
    private String info;

    /**
     * Nazwa sali (np. jej numer).
     */
    @NotBlank
    private String classroomName;
}
