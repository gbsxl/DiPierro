package Di.Pierro.application.port.output;

import Di.Pierro.domain.model.DocumentMention;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentMentionRepository {
    void save(DocumentMention documentMention, UUID documentId, UUID actorId);
    Optional<DocumentMention> findById(UUID id);
    List<DocumentMention> findAll();
}
