package douglas.events.infraestructure.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import douglas.events.infraestructure.model.enums.EnrollmentStatus;
import douglas.events.infraestructure.model.enums.WasPresent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


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

    @Version
    private Long version;

    @Column(unique = true)
    private String numericCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    private LocalDateTime enrollmentDate;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @Enumerated(EnumType.STRING)
    private WasPresent wasPresent = WasPresent.PENDING;

    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Certificate certificate;

    @Column(nullable = false, unique = true)
    private String validationToken;

    @PrePersist
    public void generateValidationToken() {
        this.validationToken = UUID.randomUUID().toString();
        this.enrollmentDate = LocalDateTime.now();
    }

}
