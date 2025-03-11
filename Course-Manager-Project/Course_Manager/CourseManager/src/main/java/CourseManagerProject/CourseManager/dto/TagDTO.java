package CourseManagerProject.CourseManager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) do przesyłania danych związanych z tworzeniem lub
 * aktualizacją obiektu typu {@link CourseManagerProject.CourseManager.model.Tag}.
 * <p>Zawiera przede wszystkim nazwę tagu.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDTO {

    /**
     * Nazwa tagu (np. "Matematyka", "Nauki Przyrodnicze").
     * Nie może być pusta (adnotacja @NotBlank).
     */
    @NotBlank
    private String name;
}
