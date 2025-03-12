package CourseManagerProject.CourseManager.controller;

import CourseManagerProject.CourseManager.dto.TagDTO;
import CourseManagerProject.CourseManager.model.Tag;
import CourseManagerProject.CourseManager.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler REST zarządzający operacjami na encjach {@link Tag}.
 * <p>Zapewnia endpointy do tworzenia, pobierania, aktualizowania i usuwania tagów.</p>
 */
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * Tworzy nowy tag na podstawie informacji zawartych w obiekcie {@link TagDTO}.
     *
     * @param dto Obiekt DTO zawierający dane nowego tagu, poddane walidacji (@Validated).
     * @return Odpowiedź HTTP zawierająca utworzony obiekt {@link Tag}.
     */
    @PostMapping
    public ResponseEntity<Tag> createTag(@Validated @RequestBody TagDTO dto) {
        Tag tag = tagService.addTag(dto);
        return ResponseEntity.ok(tag);
    }

    /**
     * Pobiera tag o podanym identyfikatorze.
     *
     * @param id Unikalny identyfikator tagu w bazie danych.
     * @return Odpowiedź HTTP z obiektem {@link Tag}, jeśli znaleziony; w przeciwnym razie status 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTag(@PathVariable Integer id) {
        Tag tag = tagService.getTagById(id);
        return ResponseEntity.ok(tag);
    }

    /**
     * Aktualizuje istniejący tag na podstawie danych z obiektu {@link TagDTO}.
     *
     * @param id  Unikalny identyfikator tagu.
     * @param dto Obiekt DTO z nowymi danymi do aktualizacji.
     * @return Odpowiedź HTTP zawierająca zaktualizowany obiekt {@link Tag}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Integer id,
                                         @Validated @RequestBody TagDTO dto) {
        Tag updated = tagService.updateTag(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Usuwa tag o podanym identyfikatorze.
     *
     * @param id Unikalny identyfikator tagu do usunięcia.
     * @return Odpowiedź HTTP z komunikatem o powodzeniu (status 200),
     * lub informacją o błędzie (status 400 lub 404), w zależności od implementacji serwisu.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable Integer id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok("Tag deleted");
    }

    /**
     * Pobiera listę wszystkich tagów dostępnych w systemie.
     *
     * @return Odpowiedź HTTP zawierająca listę obiektów {@link Tag}.
     */
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }
}
