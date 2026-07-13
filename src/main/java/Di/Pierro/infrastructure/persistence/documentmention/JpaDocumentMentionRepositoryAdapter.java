package Di.Pierro.infrastructure.persistence.documentmention;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.DocumentMentionRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Document;
import Di.Pierro.domain.model.DocumentMention;
import Di.Pierro.infrastructure.mapper.DocumentMapper;
import Di.Pierro.infrastructure.mapper.DocumentMentionMapper;
import Di.Pierro.infrastructure.persistence.document.JpaDocumentRepository;
import Di.Pierro.infrastructure.persistence.entity.DocumentEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaDocumentMentionRepositoryAdapter implements DocumentMentionRepository {
    ActorRepository actorRepository;
    JpaDocumentRepository jpaDocumentRepository;
    JpaDocumentMentionRepository jpaDocumentMentionRepository;
    DocumentMentionMapper documentMentionMapper;
    DocumentMapper documentMapper;

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
}
