package Di.Pierro.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DocumentEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "hash", length = 64)
    private String hash;

    @Column(name = "extracted", nullable = false)
    private boolean extracted;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "public_procurement_id")
    private PublicProcurementEntity publicProcurement;
}
