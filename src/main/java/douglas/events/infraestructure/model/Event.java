package douglas.events.infraestructure.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import douglas.events.infraestructure.model.enums.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    private LocalDateTime initialDateTime;

    private LocalDateTime finalDateTime;

    private String local;

    Integer workload;

    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.UPCOMING;

    private Boolean isActive;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    private List<Enrollment> enrollments = new ArrayList<>();
}