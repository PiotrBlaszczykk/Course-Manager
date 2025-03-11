package CourseManagerProject.CourseManager.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) służący do rejestracji nowego użytkownika
 * w systemie {@link CourseManagerProject.CourseManager.model.User}.
 * <p>Zawiera podstawowe dane takie jak imię, nazwisko, wiek, email,
 * hasło oraz flagę wskazującą, czy użytkownik jest organizatorem.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDTO {

    /**
     * Imię użytkownika. Nie może być puste.
     */
    @NotBlank
    private String firstname;

    /**
     * Nazwisko użytkownika. Nie może być puste.
     */
    @NotBlank
    private String surname;

    /**
     * Wiek użytkownika. Musi być liczbą nieujemną.
     */
    @NotNull
    @Min(0)
    private Integer age;

    /**
     * Adres email użytkownika. Nie może być pusty i musi być
     * w formacie email.
     */
    @NotBlank
    @Email
    private String email;

    /**
     * Hasło użytkownika. Musi mieć co najmniej 8 znaków.
     */
    @NotBlank
    @Size(min = 8)
    private String password;

    /**
     * Określa, czy użytkownik jest organizatorem (true) czy uczestnikiem (false).
     */
    @NotNull
    private Boolean isOrganizer;
}
