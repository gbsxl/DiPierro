package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.document.CreateDocumentInput;
import Di.Pierro.domain.model.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentUseCases {
    void createDocument(CreateDocumentInput createDocumentInput);
    List<Document> findAll();
    Optional<Document> findById(UUID id);
    List<Document> findByPublicProcurementId(UUID id);
    List<Document> findByName(String string);
    List<Document> findByType(String string);
    List<Document> findByExtracted(boolean extracted);
    Document updateById(UUID id, CreateDocumentInput document);
    void deleteById(UUID id);
}
