package Di.Pierro.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "assets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AssetEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "type", nullable = false, length = 30)
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "estimated_value")
    private BigDecimal estimatedValue;

    @Column(name = "source")
    private String source;

    @Column(name = "still_have_it")
    private boolean stillHaveIt;

    @Column(name = "acquired_at")
    private LocalDate acquiredAt;

    @Column(name = "mapped_at")
    private LocalDate mappedAt;

    @ManyToOne
    @JoinColumn(name = "people_id", nullable = false)
    private PersonEntity person;
}
