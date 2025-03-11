package CourseManagerProject.CourseManager.repository;

import CourseManagerProject.CourseManager.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repozytorium Spring Data JPA do operacji CRUD na encjach {@link Classroom}.
 * <p>Umożliwia dodatkowo wyszukiwanie sali po jej nazwie.</p>
 */
@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    /**
     * Wyszukuje obiekt {@link Classroom} na podstawie nazwy sali.
     *
     * @param classroomName Nazwa (numer) sali.
     * @return Obiekt klasy {@link Classroom} opakowany w {@link Optional},
     *         jeśli istnieje w bazie danych; w przeciwnym razie {@link Optional#empty()}.
     */
    Optional<Classroom> findByClassroomName(String classroomName);
}
