package Di.Pierro.infrastructure.persistence.document;

import Di.Pierro.infrastructure.persistence.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaDocumentRepository extends JpaRepository<DocumentEntity, UUID> {
    List<DocumentEntity> findByPublicProcurementId(UUID publicProcurementId);
    List<DocumentEntity> findTop100ByNameContainingIgnoreCase(String name);
    List<DocumentEntity> findTop100ByTypeContainingIgnoreCase(String type);
    List<DocumentEntity> findByExtracted(boolean extracted);
}
