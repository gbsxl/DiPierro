package Di.Pierro.application.port.output;

import Di.Pierro.domain.model.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository {
    void save(Document document, UUID publicProcurementId);
    Optional<Document> findById(UUID id);
    List<Document> findAll();
}
