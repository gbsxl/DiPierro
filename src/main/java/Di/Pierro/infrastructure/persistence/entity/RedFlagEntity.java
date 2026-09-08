package Di.Pierro.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "red_flags")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RedFlagEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "type", length = 255)
    private String type;

    @Column(name = "severity", nullable = false)
    private Integer severity;

    @Column(name = "description")
    private String description;

    @Column(name = "detected_at", nullable = false)
    private OffsetDateTime detectedAt;

    @ManyToMany
    @JoinTable(
        name = "red_flags_actors",
        joinColumns = @JoinColumn(name = "red_flag_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @Builder.Default
    private List<ActorEntity> actors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "public_procurement_id")
    private PublicProcurementEntity publicProcurement;

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "association_id")
    private UUID associationId;
}
