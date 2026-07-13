package Di.Pierro.infrastructure.persistence.document;

import Di.Pierro.infrastructure.persistence.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaDocumentRepository extends JpaRepository<DocumentEntity, UUID> {
}
