package douglas.events.infraestructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "classification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Classification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String coClassification;

    @Column(nullable = false)
    private String coClassificationType;
}
