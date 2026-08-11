package Di.Pierro.infrastructure.persistence.document;

import Di.Pierro.application.dto.document.CreateDocumentInput;
import Di.Pierro.application.port.output.DocumentRepository;
import Di.Pierro.domain.model.Document;
import Di.Pierro.domain.model.PublicProcurement;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;
import Di.Pierro.infrastructure.mapper.DocumentMapper;
import Di.Pierro.infrastructure.mapper.PublicProcurementMapper;
import Di.Pierro.infrastructure.persistence.entity.DocumentEntity;
import Di.Pierro.infrastructure.persistence.entity.PublicProcurementEntity;
import Di.Pierro.infrastructure.persistence.publicprocurement.JpaPublicProcurementRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaDocumentRepositoryAdapter implements DocumentRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaDocumentRepositoryAdapter.class);

    private final JpaPublicProcurementRepository jpaPublicProcurementRepository;
    private final JpaDocumentRepository jpaDocumentRepository;
    private final DocumentMapper documentMapper;
    private final PublicProcurementMapper publicProcurementMapper;

    @Override
    public void save(Document document, UUID publicProcurementId) {
        PublicProcurementEntity procurementEntity = jpaPublicProcurementRepository.findById(publicProcurementId)
                .orElseThrow(() -> ResourceNotFoundException.of("document.procurement-not-found", "PublicProcurement", publicProcurementId));
        PublicProcurement publicProcurement = publicProcurementMapper.toDomain(procurementEntity);
        document.setPublicProcurement(publicProcurement);
        log.debug("Saving document for publicProcurementId={}", publicProcurementId);
        jpaDocumentRepository.save(documentMapper.toEntity(document));
    }

    @Override
    public Optional<Document> findById(UUID id) {
        log.debug("Looking up document by id={}", id);
        return jpaDocumentRepository.findById(id).map(documentMapper::toDomain);
    }

    @Override
    public List<Document> findAll() {
        log.debug("Fetching all documents");
        return jpaDocumentRepository.findAll().stream().map(documentMapper::toDomain).toList();
    }

    @Override
    public List<Document> findByPublicProcurementId(UUID id) {
        log.debug("Searching documents by publicProcurementId={}", id);
        return map(jpaDocumentRepository.findByPublicProcurementId(id));
    }

    @Override
    public List<Document> findByName(String string) {
        if (string == null || string.trim().length() < 3) {
            return List.of();
        }
        log.debug("Searching documents by name containing '{}'", string.trim());
        return map(jpaDocumentRepository.findTop100ByNameContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Document> findByType(String string) {
        if (string == null || string.isBlank()) {
            return List.of();
        }
        log.debug("Searching documents by type containing '{}'", string.trim());
        return map(jpaDocumentRepository.findTop100ByTypeContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Document> findByExtracted(boolean extracted) {
        log.debug("Searching documents by extracted={}", extracted);
        return map(jpaDocumentRepository.findByExtracted(extracted));
    }

    @Override
    public Document updateById(UUID id, CreateDocumentInput document) {
        DocumentEntity entity = jpaDocumentRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("document.not-found", "Document", id));

        entity.setName(document.name());
        entity.setType(document.type());
        entity.setFilePath(document.filePath());
        entity.setHash(document.hash());
        entity.setExtracted(document.extracted());

        log.debug("Updating document id={}", id);
        jpaDocumentRepository.save(entity);
        return documentMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting document id={}", id);
        jpaDocumentRepository.deleteById(id);
    }

    private List<Document> map(List<DocumentEntity> entities) {
        return entities.stream().map(documentMapper::toDomain).toList();
    }
}
