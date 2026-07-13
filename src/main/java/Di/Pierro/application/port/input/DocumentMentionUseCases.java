package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.documentmention.CreateDocumentMentionInput;
import Di.Pierro.domain.model.DocumentMention;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentMentionUseCases {
    void createDocumentMention(CreateDocumentMentionInput createDocumentMentionInput);
    List<DocumentMention> findAll();
    Optional<DocumentMention> findById(UUID id);
}
