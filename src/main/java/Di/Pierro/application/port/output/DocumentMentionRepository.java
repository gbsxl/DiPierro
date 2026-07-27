package Di.Pierro.application.port.output;

import Di.Pierro.application.dto.documentmention.CreateDocumentMentionInput;
import Di.Pierro.domain.model.DocumentMention;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentMentionRepository {
    void save(DocumentMention documentMention, UUID documentId, UUID actorId);
    Optional<DocumentMention> findById(UUID id);
    List<DocumentMention> findAll();
    List<DocumentMention> findByActorId(UUID id);
    List<DocumentMention> findByDocumentId(UUID id);
    List<DocumentMention> findByExtractedName(String string);
    DocumentMention updateById(UUID id, CreateDocumentMentionInput documentMention);
    void deleteById(UUID id);
}
