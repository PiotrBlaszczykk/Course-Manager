package CourseManagerProject.CourseManager.service;

import CourseManagerProject.CourseManager.dto.UserRegistrationDTO;
import CourseManagerProject.CourseManager.dto.UserUpdateDTO;
import CourseManagerProject.CourseManager.model.User;
import CourseManagerProject.CourseManager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serwis odpowiedzialny za logikę biznesową związaną z użytkownikami (encja {@link User}).
 * <p>Oferuje operacje rejestracji, aktualizacji, usuwania, a także
 * pobierania użytkowników z bazy danych.</p>
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    // TODO dodać passwordEncoder

    /**
     * Rejestruje nowego użytkownika na podstawie danych z {@link UserRegistrationDTO}.
     *
     * @param dto Obiekt zawierający dane użytkownika, takie jak imię,
     *            nazwisko, email, hasło i rola organizatora.
     * @return Zapisana w bazie encja {@link User}.
     * @throws IllegalArgumentException jeśli email jest już zajęty.
     */
    public User registerUser(UserRegistrationDTO dto) {
        Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = User.builder()
                .firstname(dto.getFirstname())
                .surname(dto.getSurname())
                .age(dto.getAge())
                .email(dto.getEmail())
                .password(dto.getPassword()) // TODO dodać np. passwordEncoder.encode(dto.getPassword())
                .isOrganizer(dto.getIsOrganizer())
                .build();

        return userRepository.save(user);
    }

    /**
     * Sprawdza, czy użytkownik o podanym ID jest organizatorem.
     *
     * @param organizerId ID organizatora.
     * @return Encja {@link User} reprezentująca organizatora.
     * @throws IllegalArgumentException jeśli użytkownik nie istnieje lub nie jest organizatorem.
     */
    public User getOrganizer(Integer organizerId) {
        User user = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));
        if (!Boolean.TRUE.equals(user.getIsOrganizer())) {
            throw new IllegalArgumentException("User is not an organizer");
        }
        return user;
    }

    /**
     * Pobiera listę wszystkich użytkowników w bazie danych.
     *
     * @return Lista obiektów {@link User}.
     */
    public List<User> getAll(){
        return userRepository.findAll();
    }

    /**
     * Pobiera użytkownika o podanym ID.
     *
     * @param id ID użytkownika.
     * @return {@link Optional} zawierający encję {@link User}, jeśli istnieje.
     */
    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    /**
     * Pobiera użytkownika na podstawie adresu email.
     *
     * @param email Adres email użytkownika.
     * @return {@link Optional} z obiektem {@link User}, jeśli istnieje.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Tworzy nowego użytkownika (bez rejestracji, np. do celów administracyjnych).
     *
     * @param user Encja {@link User} z danymi użytkownika.
     * @return Zapisany w bazie obiekt {@link User}.
     * @throws IllegalArgumentException jeśli email jest już w użyciu.
     */
    @Transactional
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Użytkownik z podanym adresem email już istnieje.");
        }
        return userRepository.save(user);
    }

    /**
     * Aktualizuje dane użytkownika na podstawie przesłanego DTO.
     *
     * @param id            ID istniejącego użytkownika.
     * @param userUpdateDTO Obiekt {@link UserUpdateDTO} z nowymi danymi.
     * @return Zaktualizowana encja {@link User}.
     * @throws IllegalArgumentException jeśli użytkownik nie istnieje.
     */
    @Transactional
    public User updateUser(Integer id, UserUpdateDTO userUpdateDTO) {
        return userRepository.findById(id).map(existingUser -> {
            // Aktualizujemy tylko pola, które zostały przesłane w DTO
            if (userUpdateDTO.getFirstname() != null) {
                existingUser.setFirstname(userUpdateDTO.getFirstname());
            }
            if (userUpdateDTO.getSurname() != null) {
                existingUser.setSurname(userUpdateDTO.getSurname());
            }
            if (userUpdateDTO.getAge() != null) {
                existingUser.setAge(userUpdateDTO.getAge());
            }
            if (userUpdateDTO.getEmail() != null) {
                existingUser.setEmail(userUpdateDTO.getEmail());
            }
            if (userUpdateDTO.getPassword() != null) {
                existingUser.setPassword(userUpdateDTO.getPassword());
            }
            if (userUpdateDTO.getIsOrganizer() != null) {
                existingUser.setIsOrganizer(userUpdateDTO.getIsOrganizer());
            }

            // Zapisujemy zmiany w bazie
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


    /**
     * Usuwa użytkownika o podanym ID.
     *
     * @param id ID użytkownika do usunięcia.
     * @throws IllegalArgumentException jeśli użytkownik o podanym ID nie istnieje.
     */
    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Użytkownik o podanym ID nie istnieje.");
        }
        userRepository.deleteById(id);
    }
}
