package Di.Pierro.infrastructure.persistence.documentmention;

import Di.Pierro.application.dto.documentmention.CreateDocumentMentionInput;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.DocumentMentionRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Document;
import Di.Pierro.domain.model.DocumentMention;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;
import Di.Pierro.infrastructure.mapper.DocumentMapper;
import Di.Pierro.infrastructure.mapper.DocumentMentionMapper;
import Di.Pierro.infrastructure.persistence.document.JpaDocumentRepository;
import Di.Pierro.infrastructure.persistence.entity.DocumentEntity;
import Di.Pierro.infrastructure.persistence.entity.DocumentMentionEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaDocumentMentionRepositoryAdapter implements DocumentMentionRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaDocumentMentionRepositoryAdapter.class);

    private final ActorRepository actorRepository;
    private final JpaDocumentRepository jpaDocumentRepository;
    private final JpaDocumentMentionRepository jpaDocumentMentionRepository;
    private final DocumentMentionMapper documentMentionMapper;
    private final DocumentMapper documentMapper;

    @Override
    public void save(DocumentMention documentMention, UUID documentId, UUID actorId) {
        DocumentEntity documentEntity = jpaDocumentRepository.findById(documentId)
                .orElseThrow(() -> ResourceNotFoundException.of("document-mention.document-not-found", "Document", documentId));
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> ResourceNotFoundException.of("document-mention.actor-not-found", "Actor", actorId));

        Document document = documentMapper.toDomain(documentEntity);
        documentMention.setDocument(document);
        documentMention.setActor(actor);
        log.debug("Saving document mention for documentId={} actorId={}", documentId, actorId);
        jpaDocumentMentionRepository.save(documentMentionMapper.toEntity(documentMention));
    }

    @Override
    public Optional<DocumentMention> findById(UUID id) {
        log.debug("Looking up document mention by id={}", id);
        return jpaDocumentMentionRepository.findById(id).map(documentMentionMapper::toDomain);
    }

    @Override
    public List<DocumentMention> findAll() {
        log.debug("Fetching all document mentions");
        return jpaDocumentMentionRepository.findAll().stream().map(documentMentionMapper::toDomain).toList();
    }

    @Override
    public List<DocumentMention> findByActorId(UUID id) {
        log.debug("Searching document mentions by actorId={}", id);
        return map(jpaDocumentMentionRepository.findByActorId(id));
    }

    @Override
    public List<DocumentMention> findByDocumentId(UUID id) {
        log.debug("Searching document mentions by documentId={}", id);
        return map(jpaDocumentMentionRepository.findByDocumentId(id));
    }

    @Override
    public List<DocumentMention> findByExtractedName(String string) {
        if (string == null || string.trim().length() < 3) {
            return List.of();
        }
        log.debug("Searching document mentions by extractedName containing '{}'", string.trim());
        return map(jpaDocumentMentionRepository.findTop100ByExtractedNameContainingIgnoreCase(string.trim()));
    }

    @Override
    public DocumentMention updateById(UUID id, CreateDocumentMentionInput documentMention) {
        DocumentMentionEntity entity = jpaDocumentMentionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("document-mention.not-found", "DocumentMention", id));

        entity.setRole(documentMention.role());
        entity.setConfidence(documentMention.confidence());
        entity.setExtractedName(documentMention.extractedName());

        log.debug("Updating document mention id={}", id);
        jpaDocumentMentionRepository.save(entity);
        return documentMentionMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting document mention id={}", id);
        jpaDocumentMentionRepository.deleteById(id);
    }

    private List<DocumentMention> map(List<DocumentMentionEntity> entities) {
        return entities.stream().map(documentMentionMapper::toDomain).toList();
    }
}
