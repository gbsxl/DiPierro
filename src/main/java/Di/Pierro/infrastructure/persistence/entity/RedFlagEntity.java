package Di.Pierro.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
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

    @ManyToOne
    @JoinColumn(name = "actors_id")
    private ActorEntity actor;

    @ManyToOne
    @JoinColumn(name = "public_procurement_id")
    private PublicProcurementEntity publicProcurement;

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "association_id")
    private UUID associationId;
}
