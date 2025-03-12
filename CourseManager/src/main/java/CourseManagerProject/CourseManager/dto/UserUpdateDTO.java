package CourseManagerProject.CourseManager.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {

    /**
     * Imię użytkownika.
     */
    private String firstname;

    /**
     * Nazwisko użytkownika.
     */
    private String surname;

    /**
     * Wiek użytkownika. Musi być liczbą nieujemną.
     */
    @Min(0)
    private Integer age;

    /**
     * Adres email użytkownika.
     */
    @Email
    private String email;

    /**
     * Hasło użytkownika (opcjonalne).
     */
    @Size(min = 8)
    private String password;

    /**
     * Określa, czy użytkownik jest organizatorem.
     */
    private Boolean isOrganizer;
}
