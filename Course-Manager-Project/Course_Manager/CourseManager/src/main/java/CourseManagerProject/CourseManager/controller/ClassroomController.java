package CourseManagerProject.CourseManager.controller;

import CourseManagerProject.CourseManager.dto.ClassroomDTO;
import CourseManagerProject.CourseManager.model.Classroom;
import CourseManagerProject.CourseManager.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler REST odpowiedzialny za zarządzanie salami lekcyjnymi.
 * <p>Udostępnia endpointy do tworzenia, pobierania, aktualizowania
 * oraz usuwania zasobów typu {@link Classroom}.</p>
 */
@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    /**
     * Tworzy nową salę lekcyjną na podstawie danych z DTO.
     *
     * @param dto Obiekt {@link ClassroomDTO} zawierający dane sali.
     * @return Obiekt {@link Classroom} zapisany w bazie danych.
     */
    @PostMapping
    public ResponseEntity<Classroom> createClassroom(@Validated @RequestBody ClassroomDTO dto) {
        Classroom classroom = classroomService.addClassroom(dto);
        return ResponseEntity.ok(classroom);
    }

    /**
     * Pobiera salę lekcyjną o określonym ID.
     *
     * @param id Unikalny identyfikator sali.
     * @return Obiekt {@link Classroom} o podanym ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Classroom> getClassroom(@PathVariable Integer id) {
        Classroom classroom = classroomService.getClassroomById(id);
        return ResponseEntity.ok(classroom);
    }

    /**
     * Aktualizuje istniejącą salę lekcyjną.
     *
     * @param id  Unikalny identyfikator sali.
     * @param dto Obiekt {@link ClassroomDTO} z nowymi danymi do aktualizacji.
     * @return Zaktualizowany obiekt {@link Classroom}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Classroom> updateClassroom(@PathVariable Integer id,
                                                     @Validated @RequestBody ClassroomDTO dto) {
        Classroom updated = classroomService.updateClassroom(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Usuwa salę lekcyjną o podanym ID z bazy danych.
     *
     * @param id Unikalny identyfikator sali.
     * @return Komunikat potwierdzający usunięcie sali.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClassroom(@PathVariable Integer id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.ok("Classroom deleted");
    }

    /**
     * Pobiera listę wszystkich dostępnych sal lekcyjnych.
     *
     * @return Lista obiektów {@link Classroom}.
     */
    @GetMapping
    public ResponseEntity<List<Classroom>> getAllClassrooms() {
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        return ResponseEntity.ok(classrooms);
    }



    // do celów testowych
    @GetMapping("/classrooms/first")
    public ResponseEntity<Classroom> getFirstClassroom() {
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        if (!classrooms.isEmpty()) {
            return ResponseEntity.ok(classrooms.get(0)); // Zwraca pierwszy element
        } else {
            return ResponseEntity.noContent().build(); // Zwraca pustą odpowiedź, jeśli brak danych
        }
    }

}
