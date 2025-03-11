package CourseManagerProject.CourseManager.repository;

import CourseManagerProject.CourseManager.model.Event;
import CourseManagerProject.CourseManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repozytorium Spring Data JPA dla encji {@link User}.
 * <p>Oferuje operacje CRUD oraz wyszukiwanie użytkownika po emailu
 * i filtrację według roli (organizator/uczestnik).</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Wyszukuje użytkownika na podstawie adresu email.
     *
     * @param email Adres email (np. "jan@example.com").
     * @return Obiekt {@link User} opakowany w {@link Optional}, jeśli istnieje; w przeciwnym razie pusty optional.
     */
    Optional<User> findByEmail(String email);

    /**
     * Zwraca listę użytkowników w zależności od tego, czy są organizatorami.
     *
     * @param bool flaga (true jeśli organizer, false jeśli uczestnik).
     * @return Lista obiektów {@link User}.
     */
    List<User> findByIsOrganizer(boolean bool);
}
