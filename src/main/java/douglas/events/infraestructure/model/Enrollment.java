package douglas.events.infraestructure.model;

import douglas.events.infraestructure.model.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "participant_id"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Enrollment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "event_id", nullable = false)
        private Event event;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "participant_id", nullable = false)
        private Participant participant;

        private LocalDateTime enrollmentDate;

        @Enumerated(EnumType.STRING)
        private EnrollmentStatus status;

        @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL)
        private Certificate certificate;

}
