package CourseManagerProject.CourseManager.initializer;

import CourseManagerProject.CourseManager.model.Classroom;
import CourseManagerProject.CourseManager.model.Event;
import CourseManagerProject.CourseManager.model.Tag;
import CourseManagerProject.CourseManager.model.User;
import CourseManagerProject.CourseManager.repository.ClassroomRepository;
import CourseManagerProject.CourseManager.repository.EventRepository;
import CourseManagerProject.CourseManager.repository.TagRepository;
import CourseManagerProject.CourseManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Klasa odpowiedzialna za inicjalizację przykładowych danych w bazie
 * zaraz po uruchomieniu aplikacji (poprzez {@link CommandLineRunner}).
 * <p>Usuwa istniejące rekordy, a następnie zapisuje przykładowych
 * użytkowników, sale oraz tagi do bazy.</p>
 */
@Component
public class SampleDataInitializer implements CommandLineRunner {

    private final List<User> sampleUsers;
    private final List<Classroom> sampleClassrooms;
    private final List<Tag> sampleTags;
    private final List<Event> sampleEvents;

    private final UserRepository userRepository;
    private final ClassroomRepository classroomRepository;
    private final TagRepository tagRepository;
    private final EventRepository eventRepository;

    /**
     * Konstruktor przyjmujący listy przykładowych obiektów i repozytoria,
     * do których zostaną one zapisane.
     *
     * @param sampleUsers      Lista przykładowych obiektów {@link User}.
     * @param sampleClassrooms Lista przykładowych obiektów {@link Classroom}.
     * @param sampleTags       Lista przykładowych obiektów {@link Tag}.
     * @param userRepository   Repozytorium użytkowników.
     * @param classroomRepository Repozytorium sal.
     * @param tagRepository    Repozytorium tagów.
     */
    @Autowired
    public SampleDataInitializer(
            List<User> sampleUsers,
            List<Classroom> sampleClassrooms,
            List<Tag> sampleTags,
            List<Event> sampleEvents,
            UserRepository userRepository,
            ClassroomRepository classroomRepository,
            TagRepository tagRepository,
            EventRepository eventRepository) {
        this.sampleUsers = sampleUsers;
        this.sampleClassrooms = sampleClassrooms;
        this.sampleTags = sampleTags;
        this.sampleEvents = sampleEvents;
        this.userRepository = userRepository;
        this.classroomRepository = classroomRepository;
        this.tagRepository = tagRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Metoda uruchamiana po starcie aplikacji.
     * <p>Usuwa istniejące rekordy w bazie (użytkowników, sale i tagi),
     * a następnie zapisuje dostarczone listy przykładowych danych.</p>
     *
     * @param args Argumenty wiersza poleceń (niewykorzystane).
     */
    @Override
    public void run(String... args) {
        // Usuwanie istniejących danych
        userRepository.deleteAll();
        classroomRepository.deleteAll();
        tagRepository.deleteAll();
        eventRepository.deleteAll();

        System.out.println("Baza danych została wyczyszczona.");

        // Zapisanie sal lekcyjnych
        classroomRepository.saveAll(sampleClassrooms);
        System.out.println("Zapisano sale lekcyjne do bazy danych.");

        // Zapisanie użytkowników
        userRepository.saveAll(sampleUsers);
        System.out.println("Zapisano użytkowników do bazy danych.");

        // Zapisanie tagów
        tagRepository.saveAll(sampleTags);
        System.out.println("Zapisano tagi do bazy danych.");

        // Zapisanie wydarzeń
        eventRepository.saveAll(sampleEvents);
        System.out.println("Zapisano wydarzenia do bazy danych.");
    }
}
