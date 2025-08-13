package douglas.events.infraestructure.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "activities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference
    private Event event;

    @Column(nullable = false)
    private String name;

    private String description;

    private LocalDate dateTime;

    private Integer duration;

    private String local;
}
