package CourseManagerProject.CourseManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO (Data Transfer Object) dla encji Event.
 * Zawiera tylko niezbędne dane do przesyłania między backendem a frontendem.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    private Integer id;
    private String name;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private Integer maxParticipants;
    private Integer minAge;
    private String info;
    private Integer organizerId;
    private String organizerName; // Dodatkowe pole na nazwę organizatora
    private Integer classroomId;
    private String classroomName; // Dodatkowe pole na nazwę sali
    private List<Integer> tagIds; // Lista ID tagów przypisanych do wydarzenia
}
