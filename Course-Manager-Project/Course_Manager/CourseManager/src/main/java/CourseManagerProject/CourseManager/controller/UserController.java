package CourseManagerProject.CourseManager.controller;

import CourseManagerProject.CourseManager.dto.UserRegistrationDTO;
import CourseManagerProject.CourseManager.dto.UserUpdateDTO;
import CourseManagerProject.CourseManager.model.User;
import CourseManagerProject.CourseManager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Kontroler REST zarządzający operacjami na encjach {@link User}.
 * <p>Zapewnia endpointy do rejestracji użytkownika, pobierania
 * listy i szczegółów użytkowników, aktualizowania oraz usuwania.</p>
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Konstruktor przyjmujący serwis {@link UserService}, odpowiedzialny
     * za logikę biznesową związaną z użytkownikami.
     *
     * @param service Obiekt serwisu {@link UserService}.
     */
    public UserController(UserService service) {
        this.userService = service;
    }

    /**
     * Pobiera listę wszystkich użytkowników.
     *
     * @return Odpowiedź HTTP z listą obiektów {@link User}.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Pobiera szczegóły użytkownika o podanym identyfikatorze.
     *
     * @param id ID (identyfikator) użytkownika w bazie danych.
     * @return Odpowiedź HTTP z obiektem {@link User}, jeśli istnieje;
     *         w przeciwnym razie status 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Rejestruje nowego użytkownika w systemie.
     *
     * @param dto Obiekt {@link UserRegistrationDTO} zawierający dane do rejestracji.
     * @return Odpowiedź HTTP z utworzonym obiektem {@link User}.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Validated @RequestBody UserRegistrationDTO dto) {
        User user = userService.registerUser(dto);
        return ResponseEntity.ok(user);
    }

    /**
     * Aktualizuje dane istniejącego użytkownika.
     *
     * @param id         ID użytkownika do aktualizacji.
     * @param userUpdateDTO Obiekt {@link UserUpdateDTO} z nowymi danymi.
     * @return Odpowiedź HTTP z zaktualizowanym obiektem {@link User}
     *         lub status 400 (Bad Request), jeśli operacja się nie powiodła.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            User updatedUser = userService.updateUser(id, userUpdateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    /**
     * Usuwa użytkownika o podanym ID.
     *
     * @param id ID użytkownika do usunięcia.
     * @return Odpowiedź HTTP z kodem 204 (No Content), jeśli operacja się powiodła;
     *         lub 400 (Bad Request), jeśli wystąpił błąd.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Pobiera użytkownika na podstawie adresu email.
     *
     * @param email Adres email użytkownika.
     * @return Odpowiedź HTTP z obiektem {@link User}, jeśli znaleziony;
     *         w przeciwnym razie 404 (Not Found).
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
