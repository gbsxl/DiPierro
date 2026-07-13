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
}
