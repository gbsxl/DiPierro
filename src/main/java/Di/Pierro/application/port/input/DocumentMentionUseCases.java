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
    List<DocumentMention> findByActorId(UUID id);
    List<DocumentMention> findByDocumentId(UUID id);
    List<DocumentMention> findByExtractedName(String string);
    DocumentMention updateById(UUID id, CreateDocumentMentionInput documentMention);
    void deleteById(UUID id);
}
