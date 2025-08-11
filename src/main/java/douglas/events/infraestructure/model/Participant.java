package douglas.events.infraestructure.model;

import douglas.events.infraestructure.model.enums.ParticipantType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "participants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private ParticipantType participantType;

    @OneToMany(mappedBy = "participant")
    private List<Enrollment> enrollments = new ArrayList<>();
}
