package Di.Pierro.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "document_mentions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DocumentMentionEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "confidence")
    private Integer confidence;

    @Column(name = "extracted_name")
    private String extractedName;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private DocumentEntity document;

    @ManyToOne
    @JoinColumn(name = "actors_id", nullable = false)
    private ActorEntity actor;
}
