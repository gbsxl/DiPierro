package Di.Pierro.application.port.output;

import Di.Pierro.application.dto.document.CreateDocumentInput;
import Di.Pierro.domain.model.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository {
    void save(Document document, UUID publicProcurementId);
    Optional<Document> findById(UUID id);
    List<Document> findAll();
    List<Document> findByPublicProcurementId(UUID id);
    List<Document> findByName(String string);
    List<Document> findByType(String string);
    List<Document> findByExtracted(boolean extracted);
    Document updateById(UUID id, CreateDocumentInput document);
    void deleteById(UUID id);
}
