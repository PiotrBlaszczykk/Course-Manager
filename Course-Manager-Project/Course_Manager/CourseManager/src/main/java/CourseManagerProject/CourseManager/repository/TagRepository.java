package CourseManagerProject.CourseManager.repository;

import CourseManagerProject.CourseManager.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repozytorium Spring Data JPA dla encji {@link Tag}.
 * <p>Umożliwia wykonywanie standardowych operacji CRUD oraz
 * oferuje wyszukiwanie tagów po nazwie.</p>
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    /**
     * Wyszukuje obiekt {@link Tag} na podstawie nazwy.
     *
     * @param name Nazwa tagu (np. "Matematyka").
     * @return Obiekt {@link Tag} opakowany w {@link Optional}, jeśli istnieje; w przeciwnym razie {@link Optional#empty()}.
     */
    Optional<Tag> findByName(String name);
}
