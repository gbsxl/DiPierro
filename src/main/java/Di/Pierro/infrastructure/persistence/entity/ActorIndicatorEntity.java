package Di.Pierro.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "actor_indicators")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActorIndicatorEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "indicator_type", nullable = false, length = 150)
    private String indicatorType;

    @Column(name = "value", nullable = false, length = 150)
    private String value;

    @Column(name = "source")
    private String source;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "actors_id", nullable = false)
    private ActorEntity actor;
}
