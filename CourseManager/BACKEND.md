# Backend w Projekcie Course Manager

## Spis Treści

1. [Przygotowanie Środowiska](#przygotowanie-środowiska)
2. [Konfiguracja Zabezpieczeń (Spring Security z JWT)](#konfiguracja-zabezpieczeń-spring-security-z-jwt)
3. [Tworzenie Repository](#tworzenie-repository)
4. [Implementacja Warstwy Serwisowej](#implementacja-warstwy-serwisowej)
5. [Tworzenie Kontrolerów REST](#tworzenie-kontrolerów-rest)
6. [Implementacja Rejestracji i Logowania Użytkownika](#implementacja-rejestracji-i-logowania-użytkownika)
7. [Zarządzanie Kursami i Uczestnictwem](#zarządzanie-kursami-i-uczestnictwem)
8. [Tworzenie Zajęć przez Organizatora](#tworzenie-zajęć-przez-organizatora)
9. [Generowanie Dokumentacji Javadoc](#generowanie-dokumentacji-javadoc)
10. [Testowanie i Walidacja](#testowanie-i-walidacja)

---

## Przygotowanie Środowiska

- Aktualizacja zależności w pliku `build.gradle`, włączając Spring Security, Spring Data JPA, JWT oraz inne niezbędne biblioteki.
- Skonfigurowanie połączenia z bazą danych poprzez ustawienia w pliku `application.properties`.

## Konfiguracja Zabezpieczeń (Spring Security z JWT)

- Stworzenie klasy konfiguracyjnej zabezpieczeń, definiującej reguły dostępu do endpointów.
- Implementacja mechanizmu JWT, obejmująca generowanie i weryfikację tokenów.
- Dodanie filtrów bezpieczeństwa odpowiedzialnych za przechwytywanie i weryfikację tokenów JWT w nagłówkach żądań HTTP.

## Tworzenie Repository

- Zdefiniowanie interfejsów Repository dla każdej encji (`User`, `Event`, `Classroom`, `Tag`), rozszerzających `JpaRepository`.
- Dodanie niestandardowych metod zapytań poprzez konwencje nazewnictwa Spring Data JPA lub adnotacje `@Query`.

## Implementacja Warstwy Serwisowej

- Stworzenie klas serwisowych dla głównych funkcjonalności:
  - Zarządzanie użytkownikami (`UserService`)
  - Zarządzanie kursami (`EventService`)
  - Zarządzanie salami (`ClassroomService`)
  - Zarządzanie tagami (`TagService`)
- Implementacja logiki biznesowej, obejmującej rejestrację, logowanie, przypisywanie kursów, filtrowanie itp.

## Tworzenie Kontrolerów REST

- Utworzenie kontrolerów REST dla poszczególnych funkcjonalności:
  - `AuthController` – Rejestracja i logowanie użytkowników.
  - `EventController` – Zarządzanie kursami i uczestnictwem.
  - `ClassroomController` – Zarządzanie salami.
  - `TagController` – Zarządzanie tagami.
- Zdefiniowanie endpointów odpowiadających wymaganym operacjom, takim jak rejestracja, logowanie, wyświetlanie kursów, tworzenie zajęć.

## Implementacja Rejestracji i Logowania Użytkownika

- **Rejestracja**:
  - Przyjęcie danych użytkownika (imię, nazwisko, email, hasło, rola).
  - Hashowanie hasła przed zapisaniem do bazy danych.
  - Zapisanie użytkownika w bazie danych.
- **Logowanie**:
  - Weryfikacja podanych danych (email i hasło).
  - Generowanie tokenu JWT po pomyślnym uwierzytelnieniu.
  - Zwrócenie tokenu do klienta.

## Zarządzanie Kursami i Uczestnictwem

- **Wyświetlanie kursów zapisanych przez użytkownika**:
  - Implementacja endpointów do pobierania kursów z przeszłości i przyszłości.
- **Wyświetlanie kursów organizowanych przez użytkownika**:
  - Implementacja endpointu do pobierania kursów, które użytkownik organizuje.
- **Filtrowanie dostępnych kursów**:
  - Implementacja możliwości filtrowania kursów według danych prowadzących, tagów, sali oraz dostępności miejsc.

## Tworzenie Zajęć przez Organizatora

- **Sprawdzenie uprawnień użytkownika**:
  - Upewnienie się, że użytkownik posiada rolę organizatora.
- **Wybór sali i terminu**:
  - Pokazanie dostępnych terminów w wybranych salach.
  - Sprawdzenie dostępności sali na wybrany termin.
- **Tworzenie kursu**:
  - Przyjęcie danych kursu, takich jak nazwa, liczba uczestników, minimalny wiek, tagi, informacje oraz przypisanie do odpowiedniej sali.
  - Zapisanie nowego kursu w bazie danych, wraz z przypisaniem do wybranych tagów i sali.

## Generowanie Dokumentacji Javadoc

- Dodanie komentarzy JavaDoc do wszystkich klas, metod i pól w kodzie źródłowym.
- Konfiguracja zadania Javadoc w pliku `build.gradle`, uwzględniająca opcje formatowania i linki do zewnętrznych dokumentacji.
- Generowanie dokumentacji poprzez uruchomienie odpowiedniego zadania budującego.
- Integracja generowania Javadoc z procesem budowania, umożliwiająca automatyczne aktualizowanie dokumentacji.

## Testowanie i Walidacja

- Napisanie testów jednostkowych i integracyjnych dla serwisów, kontrolerów oraz repository, zapewniających poprawne działanie funkcjonalności.
- Walidacja danych wejściowych przy użyciu adnotacji walidacyjnych oraz obsługa wyjątków, gwarantująca integralność danych.
- Przetestowanie zabezpieczeń, weryfikacja, że endpointy są odpowiednio chronione i dostępne tylko dla uprawnionych użytkowników.

---

## Projekt Aplikacji Course Manager

### Kontekst Aplikacji

Aplikacja służy do zarządzania kursami na uczelni, umożliwiając:

- Rejestrację i logowanie użytkowników (uczestników i organizatorów).
- Przegląd i zapis na kursy dostępne w systemie.
- Tworzenie i modyfikację kursów przez organizatorów.
- Przegląd kursów, w których użytkownik bierze udział (zarówno przeszłych, jak i przyszłych).
- Filtrowanie kursów po salach, tagach, prowadzących oraz po dostępności miejsc.

### Warstwy Architektoniczne

Aplikacja podzielona jest na trzy główne warstwy:

1. **Warstwa Prezentacji (Frontend)**:
  - Implementacja w React.
  - Komunikacja z backendem poprzez REST API.
  - Obsługa logowania, rejestracji, wyświetlania listy kursów, formularzy do tworzenia i modyfikacji kursów.

2. **Warstwa Logiki Biznesowej (Backend w Spring Boot)**:
  - Obsługa logiki rejestracji, logowania, autentykacji i autoryzacji (Spring Security + JWT).
  - Implementacja logiki biznesowej związanej z kursami, zapisami, zarządzaniem salami i tagami.
  - Warstwa serwisowa korzystająca z Repository do komunikacji z bazą danych.
  - Wystawienie REST API do komunikacji z frontendem.

3. **Warstwa Dostępu do Danych (Baza + Repository)**:
  - Baza danych (np. PostgreSQL).
  - Encje i mapowania JPA/Hibernate.
  - Repository Spring Data JPA udostępniające metody CRUD i niestandardowe zapytania.


### Model Danych

- **User**  
  Atrybuty: `id`, `firstname`, `surname`, `age`, `email`, `password`, `isOrganizer`  
  Relacje:
  - OneToMany z `Event` (jako organizer).
  - ManyToMany z `Event` (jako participant).

- **Event**  
  Atrybuty: `id`, `name`, `startDatetime`, `endDatetime`, `maxParticipants`, `minAge`, `info`  
  Relacje:
  - ManyToOne z `User` (organizer).
  - ManyToOne z `Classroom`.
  - ManyToMany z `User` (participants).
  - ManyToMany z `Tag`.

- **Classroom**  
  Atrybuty: `id`, `capacity`, `location`, `info`, `classroomName`  
  Relacje:
  - OneToMany z `Event`.

- **Tag**  
  Atrybuty: `id`, `name`  
  Relacje:
  - ManyToMany z `Event`.

### Główne Funkcjonalności i Ich Obsługa

1. **Rejestracja Użytkownika**:
  - `AuthController` przyjmuje dane użytkownika, wywołuje `UserService.registerUser()`.
  - Walidacja danych, hashowanie hasła.
  - Zapis w `UserRepository`.

2. **Logowanie i Autoryzacja**:
  - `AuthController` przy logowaniu weryfikuje hasło.
  - Wygenerowanie tokenu JWT (`JwtUtil`).
  - Filtry i konfiguracja `SecurityConfig` zapewniają dostęp do endpointów wyłącznie po dostarczeniu ważnego tokenu.

3. **Wyświetlanie Kursów Użytkownika**:
  - `EventController` zwraca listę kursów zapisanych przez użytkownika, korzystając z `EventService` i `EventRepository` (metody wyszukujące po participant_id).
  - Oddzielne endpointy dla kursów przeszłych i przyszłych (filtracja po datach).

4. **Wyświetlanie Kursów Organizatora**:
  - `EventController` zwraca listę kursów, gdzie `organizer_id` = `user.id` i `user.isOrganizer = true`.
  - Filtracja w `EventRepository` (np. `findByOrganizerId()`).

5. **Filtry Dostępnych Kursów**:
  - `EventController` umożliwia pobranie listy dostępnych kursów z parametrami zapytania (organizerId, classroomId, tagId, excludeFull).
  - `EventService` wywołuje metody w `EventRepository` z odpowiednimi zapytaniami (JPQL lub Query Methods).

6. **Tworzenie Zajęć**:
  - `EventController` endpoint `/api/events/create`.
  - Sprawdzenie `isOrganizer`.
  - Walidacja sali i terminów (`ClassroomService` i `EventService`).
  - Zapis nowego eventu w `EventRepository`.

### Przepływ Danych

1. Użytkownik (frontend) -> żądanie HTTP (REST) -> Controller (Spring Boot)
2. Controller -> Wywołanie metod serwisów (logika biznesowa)
3. Serwis -> Wywołanie metod Repository w celu pobrania/zapisania danych
4. Repository -> Operacje CRUD na bazie danych poprzez JPA/Hibernate
5. Zwrócenie wyników do serwisu, a następnie do kontrolera i dalej do frontend’u.

### Bezpieczeństwo i Role

- Wykorzystanie Spring Security + JWT do zabezpieczenia endpointów.
- Każdy request (poza rejestracją i logowaniem) wymaga validnego tokenu JWT w nagłówku.
- Weryfikacja roli (`isOrganizer`) przed zezwoleniem na tworzenie lub edycję kursów.

### Dokumentacja i Testy

- Dokumentacja za pomocą Javadoc.
- Testy jednostkowe serwisów i repository (JUnit, Mockito).
- Testy integracyjne kontrolerów (Spring Boot Test + MockMvc).

## Podsumowanie

Implementacja wymaganych funkcjonalności backendowych w projekcie Course Manager obejmuje systematyczne podejście, które integruje konfigurację zabezpieczeń, tworzenie repository, implementację logiki biznesowej w warstwie serwisowej oraz definiowanie odpowiednich endpointów w kontrolerach REST. Regularne testowanie oraz dokumentowanie kodu za pomocą Javadoc przyczyniają się do utrzymania wysokiej jakości i przejrzystości projektu.

W przypadku napotkania problemów lub potrzeby dalszych wyjaśnień, dostępne są dodatkowe zasoby oraz wsparcie techniczne.

