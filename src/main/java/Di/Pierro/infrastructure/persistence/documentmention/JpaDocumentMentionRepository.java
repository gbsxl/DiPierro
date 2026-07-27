package Di.Pierro.infrastructure.persistence.documentmention;

import Di.Pierro.infrastructure.persistence.entity.DocumentMentionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaDocumentMentionRepository extends JpaRepository<DocumentMentionEntity, UUID> {
    List<DocumentMentionEntity> findByActorId(UUID actorId);
    List<DocumentMentionEntity> findByDocumentId(UUID documentId);
    List<DocumentMentionEntity> findTop100ByExtractedNameContainingIgnoreCase(String extractedName);
}
