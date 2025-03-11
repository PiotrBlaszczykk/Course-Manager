package CourseManagerProject.CourseManager.config;

import CourseManagerProject.CourseManager.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Klasa konfiguracyjna odpowiedzialna za tworzenie przykładowych danych (beanów) aplikacji.
 * <p>Beany z tej klasy mogą służyć jako wstępne dane testowe załadowane do aplikacji
 * zaraz po jej uruchomieniu, ułatwiając testowanie funkcjonalności.</p>
 */
@Configuration
public class SampleDataConfig {

    /**
     * Tworzy obiekt {@link User} z określonymi parametrami.
     *
     * @param firstname   Imię użytkownika.
     * @param surname     Nazwisko użytkownika.
     * @param age         Wiek użytkownika.
     * @param email       Adres email użytkownika.
     * @param password    Hasło użytkownika.
     * @param isOrganizer Informacja, czy użytkownik jest organizatorem.
     * @return Utworzony obiekt klasy {@link User}.
     */
    private User createUser(String firstname, String surname, int age, String email, String password, boolean isOrganizer) {
        return User.builder()
                .firstname(firstname)
                .surname(surname)
                .age(age)
                .email(email)
                .password(password)
                .isOrganizer(isOrganizer)
                .build();
    }

    /**
     * Tworzy obiekt {@link Classroom} z określonymi parametrami.
     *
     * @param capacity       Pojemność sali.
     * @param location       Lokalizacja sali.
     * @param info           Dodatkowe informacje o sali.
     * @param classroomName  Nazwa sali (np. numer).
     * @return Utworzony obiekt klasy {@link Classroom}.
     */
    private Classroom createClassroom(int capacity, String location, String info, String classroomName) {
        return Classroom.builder()
                .capacity(capacity)
                .location(location)
                .info(info)
                .classroomName(classroomName)
                .build();
    }

    /**
     * Tworzy obiekt {@link Tag} z określoną nazwą.
     *
     * @param name Nazwa tagu (np. "Matematyka").
     * @return Utworzony obiekt klasy {@link Tag}.
     */
    private Tag createTag(String name) {
        return Tag.builder()
                .name(name)
                .build();
    }

    /**
     * Tworzy obiekt {@link Event} z określonymi parametrami, przypisując
     * uczestników oraz tagi do wydarzenia.
     *
     * @param name             Nazwa wydarzenia.
     * @param start            Data i czas rozpoczęcia.
     * @param end              Data i czas zakończenia.
     * @param maxParticipants  Maksymalna liczba uczestników.
     * @param minAge           Minimalny wiek uczestnika.
     * @param info             Dodatkowe informacje.
     * @param organizer        Organizator wydarzenia (obiekt klasy {@link User}).
     * @param classroom        Sala, w której odbywa się wydarzenie (obiekt klasy {@link Classroom}).
     * @param tags             Lista tagów przypisanych do wydarzenia.
     * @param participants     Lista uczestników wydarzenia.
     * @return Utworzony obiekt klasy {@link Event} wraz z przypisanymi tagami i uczestnikami.
     */
    private Event createEvent(String name, LocalDateTime start, LocalDateTime end, int maxParticipants, int minAge,
                              String info, User organizer, Classroom classroom, List<Tag> tags, List<User> participants) {
        Event event = Event.builder()
                .name(name)
                .startDatetime(start)
                .endDatetime(end)
                .maxParticipants(maxParticipants)
                .minAge(minAge)
                .info(info)
                .organizer(organizer)
                .classroom(classroom)
                .build();

        event.getTags().addAll(tags);
        event.getParticipants().addAll(participants);

        return event;
    }

    /**
     * Udostępnia listę przykładowych użytkowników (zarówno organizatorów, jak i uczestników).
     *
     * @return Lista obiektów klasy {@link User}.
     */
    @Bean
    public List<User> sampleUsers() {
        return List.of(
                // Organizatorzy
                createUser("Mariusz", "Ważka", 50, "MariuszW@organizer.agh.edu.pl", "MatematykaDyskretnaJestNajlepsza!1!1!1!!", true),
                createUser("Zbigniew", "Trzynoga", 45, "ZbigniewT@organizer.agh.edu.pl", "BardzoKochamPewnąPartię123", true),
                createUser("Wacław", "Odwiertniczy", 44, "WacekO@organizer.agh.edu.pl", "KrosnoByłoLepsze321", true),
                createUser("Walter", "White", 52, "WW@organizer.agh.edu.pl", "JesseWeNeedToCook555", true),
                createUser("Saul", "Goodman", 48, "SaulG@organizer.agh.edu.pl", "AdwokatDiabłaAleTakiUWU", true),
                createUser("Severus", "Snape", 35, "SevS@organizer.agh.edu.pl", "CoZrobiłSnapeZCiałemLily?", true),
                createUser("Geralt", "ZRivii", 40, "Geralt@organizer.agh.edu.pl", "YeneferNaJednorożcu69", true),
                createUser("Franklin", "Underwood", 50, "Franek@organizer.agh.edu.pl", "LiczySięPomocNajsłabszym232", true),

                // Uczestnicy
                createUser("Piotr", "Kafelkowanie", 23, "Peter@gmail.com", "qwerty123", false),
                createUser("Tomasz", "Wpiernicz", 21, "tomek@gmail.com", "mango123", false),
                createUser("Piotr", "Barszczyk", 22, "barszczykp@gmail.com", "GdyMiałem3lataDoŁóżkaWszedłMiTata", false),
                createUser("Ewa", "Miszak", 25, "ewkaB@gmail.com", "IchLiebeDich223", false),
                createUser("Piotr", "Żaba", 23, "peterZ@gmail.com", "qwerty123", false),
                createUser("Dawid", "Zabor", 22, "dawid@gmail.com", "uwu123", false),
                createUser("Włodzimierz", "Biały", 27, "wlodek@gmail.com", "zakopane555", false),
                createUser("Waldemar", "Paker", 30, "waldek@gmail.com", "białkoToMojePaliwo323", false)
        );
    }

    /**
     * Udostępnia listę przykładowych sal lekcyjnych.
     *
     * @return Lista obiektów klasy {@link Classroom}.
     */
    @Bean
    public List<Classroom> sampleClassrooms() {
        return List.of(
                createClassroom(60, "D17 AGH Campus", "Uwaga na niebezpieczną metyloaminę", "4.40"),
                createClassroom(40, "A2 AGH Campus", "None", "3.2"),
                createClassroom(30, "D17 AGH Campus", "Sala audytoryjna", "2.41"),
                createClassroom(60, "Ceramiczka", "Sala Wykładowa", "5.13")
        );
    }

    /**
     * Udostępnia listę przykładowych tagów tematycznych.
     *
     * @return Lista obiektów klasy {@link Tag}.
     */
    @Bean
    public List<Tag> sampleTags() {
        return List.of(
                createTag("Matematyka"),
                createTag("Nauki Przyrodnicze"),
                createTag("Nauki Prawnicze"),
                createTag("Nauki Urojone")
        );
    }

    /**
     * Udostępnia listę przykładowych wydarzeń, tworzonych na podstawie
     * list użytkowników, sal oraz tagów.
     *
     * @param sampleUsers      Lista użytkowników wstrzykniętych przez Springa.
     * @param sampleClassrooms Lista sal wstrzykniętych przez Springa.
     * @param sampleTags       Lista tagów wstrzykniętych przez Springa.
     * @return Lista obiektów klasy {@link Event} zawierających przykładowe kursy/zdarzenia.
     */
    @Bean
    public List<Event> sampleEvents(List<User> sampleUsers, List<Classroom> sampleClassrooms, List<Tag> sampleTags) {
        return List.of(
                createEvent(
                        "Kurs Matematyki Dyskretnej Rozszerzony",
                        LocalDateTime.of(2025, 12, 11, 10, 30),
                        LocalDateTime.of(2025, 12, 11, 15, 30),
                        50,
                        18,
                        "Wymagane zaliczenie przedmiotu Matematyka Dyskretna",
                        sampleUsers.get(0),
                        sampleClassrooms.get(2),
                        List.of(sampleTags.get(0)),
                        List.of(sampleUsers.get(8), sampleUsers.get(9), sampleUsers.get(10), sampleUsers.get(11))
                ),
                createEvent(
                        "Jak się nie narobić a zarobić",
                        LocalDateTime.of(2025, 2, 11, 12, 30),
                        LocalDateTime.of(2025, 12, 11, 16, 30),
                        50,
                        18,
                        "Trzeba być politykiem",
                        sampleUsers.get(1),
                        sampleClassrooms.get(3),
                        List.of(sampleTags.get(2)),
                        List.of(sampleUsers.get(11), sampleUsers.get(10), sampleUsers.get(13), sampleUsers.get(14))
                ),
                createEvent(
                        "Rozwiązywanie Całki Rafonixa",
                        LocalDateTime.of(2025, 11, 11, 10, 30),
                        LocalDateTime.of(2025, 11, 11, 15, 30),
                        50,
                        18,
                        "Zrozumienie teorii względności",
                        sampleUsers.get(2),
                        sampleClassrooms.get(2),
                        List.of(sampleTags.get(0)),
                        List.of(sampleUsers.get(8), sampleUsers.get(10))
                ),
                createEvent(
                        "Gotowanie Mety",
                        LocalDateTime.of(2025, 9, 13, 10, 30),
                        LocalDateTime.of(2025, 9, 13, 20, 30),
                        50,
                        18,
                        "Knowing what is wire",
                        sampleUsers.get(3),
                        sampleClassrooms.get(0),
                        List.of(sampleTags.get(1)),
                        List.of(sampleUsers.get(8), sampleUsers.get(9), sampleUsers.get(12))
                ),
                createEvent(
                        "Kurs Matematyki Dyskretnej Rozszerzony",
                        LocalDateTime.of(2025, 8, 11, 10, 30),
                        LocalDateTime.of(2025, 8, 11, 15, 30),
                        50,
                        18,
                        "Wymagane zaliczenie przedmiotu Matematyka Dyskretna",
                        sampleUsers.get(4),
                        sampleClassrooms.get(2),
                        List.of(sampleTags.get(2)),
                        List.of(sampleUsers.get(8), sampleUsers.get(9), sampleUsers.get(10), sampleUsers.get(14), sampleUsers.get(15))
                ),
                createEvent(
                        "Tworzenie eliksirów",
                        LocalDateTime.of(2025, 6, 11, 10, 30),
                        LocalDateTime.of(2025, 6, 11, 15, 30),
                        50,
                        18,
                        "Oklumencja",
                        sampleUsers.get(5),
                        sampleClassrooms.get(2),
                        List.of(sampleTags.get(3)),
                        List.of(sampleUsers.get(8), sampleUsers.get(9), sampleUsers.get(10), sampleUsers.get(14))
                ),
                createEvent(
                        "Zabijanie Strzyg",
                        LocalDateTime.of(2025, 8, 11, 8, 30),
                        LocalDateTime.of(2024, 8, 11, 15, 30), // data końcowa wstecz, zgodnie z oryg. kodem
                        50,
                        18,
                        "Kategoria A",
                        sampleUsers.get(6),
                        sampleClassrooms.get(2),
                        List.of(sampleTags.get(2)),
                        List.of(sampleUsers.get(8), sampleUsers.get(9), sampleUsers.get(10), sampleUsers.get(14), sampleUsers.get(15))
                ),
                createEvent(
                        "Jak być miłym w polityce",
                        LocalDateTime.of(2025, 12, 11, 13, 30),
                        LocalDateTime.of(2024, 12, 11, 15, 30), // data końcowa wstecz
                        50,
                        18,
                        "otwarte serce",
                        sampleUsers.get(7),
                        sampleClassrooms.get(2),
                        List.of(sampleTags.get(2)),
                        List.of(sampleUsers.get(12), sampleUsers.get(13), sampleUsers.get(10), sampleUsers.get(14), sampleUsers.get(11))
                )
        );
    }
}
