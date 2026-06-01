package Di.Pierro.infrastructure.persistence.entity;

import Di.Pierro.domain.enums.AssociationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "associations")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssociationEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "association_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssociationType associationType;

    @Column(name = "source")
    private String source;

    @Column(name = "confidence_level")
    private int confidenceLevel;

    @Column(name = "is_association_ended")
    private boolean associationEnded;

    @Column(name = "association_start", nullable = false)
    private OffsetDateTime associationStart;

    @Column(name = "association_end")
    private OffsetDateTime associationEnd;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "actor_id_first", nullable = false)
    private ActorEntity firstActor;

    @ManyToOne
    @JoinColumn(name = "actor_id_second", nullable = false)
    private ActorEntity secondActor;
}
