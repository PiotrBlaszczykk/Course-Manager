# COURSE MANAGER

## Ogólny opis projektu

Aplikacja jest stworzona w celu ułatwienia i częściowej automatyzacji procesu organizacji wydarzeń dydaktycznych takich jak kursy, webinary i podobne spotkania edukacyjne. Jest to RESTowa strona dzięki której użytkownicy chcący podjąć aktywność mogą:

- Sprawdzać dostępne terminy
- Rezerwować miejsca
- Podglądać wymagany sprzęt i wstępne umiejętności dla danych kursów
- Uzyskać wszystkie pozostałe informacje niezbędne do udziału w zajęciach

Ze strony organizatora umożliwione jest:

- Wyświetlanie obecnie już zarezerwowanych terminów
- Dodanie własnych terminów
- Dostarczanie wszelkich potrzebnych informacji w celu przeprowadzenia kursu/webinaru

## Użyte technologie

### Baza Danych

- **PostgreSQL** używany razem z **Hibernate**.

### Core projektu

- **Spring Boot**, w tym:
  - Spring Web
  - Spring Security
  - Lombok

## Użyte wzorce projektowe

- **Dependency Injection**
  - W celu zwiększenia czytelności kodu, ułatwienia przeprowadzania testów jednostkowych oraz „posiadania instrukcji tworzenia obiektów w jednym/małej ilości miejsc”.

- **MVP (Model-View-Presenter)**
  - W celu oddzielenia logiki biznesowej od warstwy wizualnej oraz zwiększenia klarowności programu.

- **ORM (Object-Relational Mapping)**
  - W celu usprawnienia komunikacji z bazą danych oraz persystencji informacji.

- **Repository**
  - W celu dodania dodatkowej warstwy abstrakcji i ukrycia odpowiedzialnego za wykonanie poszczególnych operacji kodu.

## Baza Danych

### Schemat bazy danych

[Link do schematu w Vertabelo](https://my.vertabelo.com/model/4aU7ztW3o2CO0RVew72ogxF35pZPsqNE)

### Jak uruchomić bazę?

1. **Pobierz PostgreSQL** (wersja 15) oraz **PgAdmin**.
2. Podczas konfiguracji ustaw hasło na `admin`.
3. Otwórz **PG Admin** i wykonaj następujące kroki:
   - **Username**: `postgres`
   - **Hasło**: `admin` (lub inne, które ustawiłeś podczas instalacji PGAdmina)
   - **Host**: `localhost`
   - **Port**: `5432`
   - **Nazwa bazy**: `coursemanager`

## Uruchomienie Frontendu

1. Przejdź do katalogu `frontend`:

   ```bash
   cd frontend
   ```

2. Upewnij się, że **Vite** jest zainstalowany. Jeśli nie, IDE powinno podpowiedzieć instalację.

3. Zbuduj projekt:

   ```bash
   npm build
   ```

4. Zainstaluj **Axios**:

   ```bash
   npm install axios
   ```

5. Uruchom serwer deweloperski:

   ```bash
   npm run dev
   ```

**Uwaga:** Upewnij się, że aplikacja w Springu jest uruchomiona przed startem frontendu.
