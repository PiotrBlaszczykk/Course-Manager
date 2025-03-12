package CourseManagerProject.CourseManager.service;

import CourseManagerProject.CourseManager.dto.ClassroomDTO;
import CourseManagerProject.CourseManager.model.Classroom;
import CourseManagerProject.CourseManager.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serwis odpowiedzialny za logikę biznesową związaną z salami (encja {@link Classroom}).
 * <p>Oferuje operacje tworzenia, aktualizacji, pobierania oraz usuwania sal.</p>
 */
@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    /**
     * Pobiera encję {@link Classroom} z bazy danych na podstawie jej ID.
     *
     * @param id Unikalny identyfikator sali.
     * @return Encja {@link Classroom}, jeśli istnieje.
     * @throws IllegalArgumentException jeśli sala o podanym ID nie istnieje.
     */
    public Classroom getClassroomById(Integer id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));
    }

    /**
     * Tworzy nową salę na podstawie obiektu DTO i zapisuje ją w bazie danych.
     *
     * @param dto DTO {@link ClassroomDTO} zawierające dane sali.
     * @return Utworzona i zapisana encja {@link Classroom}.
     */
    public Classroom addClassroom(ClassroomDTO dto) {
        Classroom classroom = Classroom.builder()
                .capacity(dto.getCapacity())
                .location(dto.getLocation())
                .info(dto.getInfo())
                .classroomName(dto.getClassroomName())
                .build();

        return classroomRepository.save(classroom);
    }

    /**
     * Aktualizuje istniejącą salę na podstawie obiektu DTO.
     *
     * @param id  ID istniejącej sali.
     * @param dto DTO {@link ClassroomDTO} z nowymi danymi.
     * @return Zaktualizowana encja {@link Classroom}.
     * @throws IllegalArgumentException jeśli sala o podanym ID nie istnieje.
     */
    public Classroom updateClassroom(Integer id, ClassroomDTO dto) {
        Classroom existing = getClassroomById(id);
        existing.setCapacity(dto.getCapacity());
        existing.setLocation(dto.getLocation());
        existing.setInfo(dto.getInfo());
        existing.setClassroomName(dto.getClassroomName());
        return classroomRepository.save(existing);
    }

    /**
     * Usuwa salę o podanym ID z bazy danych.
     *
     * @param id ID sali do usunięcia.
     * @throws IllegalArgumentException jeśli sala o podanym ID nie istnieje.
     */
    public void deleteClassroom(Integer id) {
        Classroom existing = getClassroomById(id);
        classroomRepository.delete(existing);
    }

    /**
     * Pobiera listę wszystkich sal w bazie danych.
     *
     * @return Lista encji {@link Classroom}.
     */
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }
}
