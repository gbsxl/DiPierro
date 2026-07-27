package Di.Pierro.infrastructure.persistence.documentmention;

import Di.Pierro.application.dto.documentmention.CreateDocumentMentionInput;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.DocumentMentionRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Document;
import Di.Pierro.domain.model.DocumentMention;
import Di.Pierro.infrastructure.mapper.DocumentMapper;
import Di.Pierro.infrastructure.mapper.DocumentMentionMapper;
import Di.Pierro.infrastructure.persistence.document.JpaDocumentRepository;
import Di.Pierro.infrastructure.persistence.entity.DocumentEntity;
import Di.Pierro.infrastructure.persistence.entity.DocumentMentionEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaDocumentMentionRepositoryAdapter implements DocumentMentionRepository {
    private final ActorRepository actorRepository;
    private final JpaDocumentRepository jpaDocumentRepository;
    private final JpaDocumentMentionRepository jpaDocumentMentionRepository;
    private final DocumentMentionMapper documentMentionMapper;
    private final DocumentMapper documentMapper;

    @Override
    public void save(DocumentMention documentMention, UUID documentId, UUID actorId) {
        Optional<DocumentEntity> optionalDocument = jpaDocumentRepository.findById(documentId);
        Optional<Actor> optionalActor = actorRepository.findById(actorId);
        if (optionalDocument.isPresent() && optionalActor.isPresent()) {
            Document document = documentMapper.toDomain(optionalDocument.get());
            documentMention.setDocument(document);
            documentMention.setActor(optionalActor.get());
            jpaDocumentMentionRepository.save(documentMentionMapper.toEntity(documentMention));
        }
    }

    @Override
    public Optional<DocumentMention> findById(UUID id) {
        return jpaDocumentMentionRepository.findById(id).map(documentMentionMapper::toDomain);
    }

    @Override
    public List<DocumentMention> findAll() {
        return jpaDocumentMentionRepository.findAll().stream().map(documentMentionMapper::toDomain).toList();
    }

    @Override
    public List<DocumentMention> findByActorId(UUID id) {
        return map(jpaDocumentMentionRepository.findByActorId(id));
    }

    @Override
    public List<DocumentMention> findByDocumentId(UUID id) {
        return map(jpaDocumentMentionRepository.findByDocumentId(id));
    }

    @Override
    public List<DocumentMention> findByExtractedName(String string) {
        if (string == null || string.trim().length() < 3) {
            return List.of();
        }

        return map(jpaDocumentMentionRepository.findTop100ByExtractedNameContainingIgnoreCase(string.trim()));
    }

    @Override
    public DocumentMention updateById(UUID id, CreateDocumentMentionInput documentMention) {
        Optional<DocumentMentionEntity> original = jpaDocumentMentionRepository.findById(id);
        original.ifPresent(value -> value.setRole(documentMention.role()));
        original.ifPresent(value -> value.setConfidence(documentMention.confidence()));
        original.ifPresent(value -> value.setExtractedName(documentMention.extractedName()));

        if (original.isPresent()) {
            jpaDocumentMentionRepository.save(original.get());
            return documentMentionMapper.toDomain(original.get());
        }

        return new DocumentMention();
    }

    @Override
    public void deleteById(UUID id) {
        jpaDocumentMentionRepository.deleteById(id);
    }

    private List<DocumentMention> map(List<DocumentMentionEntity> entities) {
        return entities.stream()
                .map(documentMentionMapper::toDomain)
                .toList();
    }
}
