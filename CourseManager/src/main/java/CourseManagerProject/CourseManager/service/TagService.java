package CourseManagerProject.CourseManager.service;

import CourseManagerProject.CourseManager.dto.TagDTO;
import CourseManagerProject.CourseManager.model.Tag;
import CourseManagerProject.CourseManager.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Serwis odpowiedzialny za logikę biznesową związaną z tagami (encja {@link Tag}).
 * <p>Oferuje operacje pobierania listy tagów, tworzenia, aktualizacji,
 * usuwania oraz weryfikacji istnienia tagów na podstawie list ID.</p>
 */
@Service
public class TagService {

    private final TagRepository tagRepository;

    /**
     * Konstruktor wstrzykujący repozytorium tagów, umożliwiające
     * wykonywanie standardowych operacji CRUD.
     *
     * @param tagRepository Repozytorium encji {@link Tag}.
     */
    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Zwraca zbiór tagów na podstawie listy ich ID.
     *
     * @param tagIds Lista ID tagów do odszukania.
     * @return Zbiór encji {@link Tag} odpowiadających podanym ID.
     * @throws IllegalArgumentException jeśli któryś z podanych ID nie istnieje w bazie.
     */
    public Set<Tag> getTagsByIds(List<Integer> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return new HashSet<>();
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new IllegalArgumentException("Some tags not found");
        }
        return new HashSet<>(tags);
    }

    /**
     * Pobiera tag z bazy na podstawie jego ID.
     *
     * @param id ID tagu.
     * @return Obiekt {@link Tag}, jeśli istnieje.
     * @throws IllegalArgumentException jeśli tag o podanym ID nie istnieje.
     */
    public Tag getTagById(Integer id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
    }

    /**
     * Tworzy nowy tag na podstawie obiektu {@link TagDTO} i zapisuje go w bazie.
     *
     * @param dto DTO zawierające dane nowego tagu (np. nazwa).
     * @return Zapisany w bazie obiekt {@link Tag}.
     */
    public Tag addTag(TagDTO dto) {
        Tag tag = Tag.builder().name(dto.getName()).build();
        return tagRepository.save(tag);
    }

    /**
     * Aktualizuje istniejący tag na podstawie obiektu {@link TagDTO}.
     *
     * @param id  ID istniejącego tagu.
     * @param dto DTO z nowymi danymi (np. nowa nazwa).
     * @return Zaktualizowany obiekt {@link Tag}.
     * @throws IllegalArgumentException jeśli tag o podanym ID nie istnieje.
     */
    public Tag updateTag(Integer id, TagDTO dto) {
        Tag existing = getTagById(id);
        existing.setName(dto.getName());
        return tagRepository.save(existing);
    }

    /**
     * Usuwa tag o podanym ID z bazy danych.
     *
     * @param id ID tagu do usunięcia.
     * @throws IllegalArgumentException jeśli tag o podanym ID nie istnieje.
     */
    public void deleteTag(Integer id) {
        Tag existing = getTagById(id);
        tagRepository.delete(existing);
    }

    /**
     * Pobiera listę wszystkich tagów z bazy danych.
     *
     * @return Lista obiektów {@link Tag}.
     */
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}
