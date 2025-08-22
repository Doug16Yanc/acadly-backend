package douglas.events.infraestructure.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "certificates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    @JsonBackReference
    private Enrollment enrollment;
    @Column(nullable = false)
    private Boolean emailSent = false;
    @Column(unique = true)
    private String validationCode;
    private LocalDateTime emissionDate;

    @PrePersist
    public void generateValidationCode() {
        this.validationCode = UUID.randomUUID().toString();
        this.emissionDate = LocalDateTime.now();
    }
}