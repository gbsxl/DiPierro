package Di.Pierro.infrastructure.persistence.document;

import Di.Pierro.application.dto.document.CreateDocumentInput;
import Di.Pierro.application.port.output.DocumentRepository;
import Di.Pierro.domain.model.Document;
import Di.Pierro.domain.model.PublicProcurement;
import Di.Pierro.infrastructure.mapper.DocumentMapper;
import Di.Pierro.infrastructure.mapper.PublicProcurementMapper;
import Di.Pierro.infrastructure.persistence.entity.DocumentEntity;
import Di.Pierro.infrastructure.persistence.entity.PublicProcurementEntity;
import Di.Pierro.infrastructure.persistence.publicprocurement.JpaPublicProcurementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaDocumentRepositoryAdapter implements DocumentRepository {
    private final JpaPublicProcurementRepository jpaPublicProcurementRepository;
    private final JpaDocumentRepository jpaDocumentRepository;
    private final DocumentMapper documentMapper;
    private final PublicProcurementMapper publicProcurementMapper;

    @Override
    public void save(Document document, UUID publicProcurementId) {
        Optional<PublicProcurementEntity> optionalPublicProcurement = jpaPublicProcurementRepository.findById(publicProcurementId);
        if (optionalPublicProcurement.isPresent()) {
            PublicProcurement publicProcurement = publicProcurementMapper.toDomain(optionalPublicProcurement.get());
            document.setPublicProcurement(publicProcurement);
            jpaDocumentRepository.save(documentMapper.toEntity(document));
        }
    }

    @Override
    public Optional<Document> findById(UUID id) {
        return jpaDocumentRepository.findById(id).map(documentMapper::toDomain);
    }

    @Override
    public List<Document> findAll() {
        return jpaDocumentRepository.findAll().stream().map(documentMapper::toDomain).toList();
    }

    @Override
    public List<Document> findByPublicProcurementId(UUID id) {
        return map(jpaDocumentRepository.findByPublicProcurementId(id));
    }

    @Override
    public List<Document> findByName(String string) {
        if (string == null || string.trim().length() < 3) {
            return List.of();
        }

        return map(jpaDocumentRepository.findTop100ByNameContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Document> findByType(String string) {
        if (string == null || string.isBlank()) {
            return List.of();
        }

        return map(jpaDocumentRepository.findTop100ByTypeContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Document> findByExtracted(boolean extracted) {
        return map(jpaDocumentRepository.findByExtracted(extracted));
    }

    @Override
    public Document updateById(UUID id, CreateDocumentInput document) {
        Optional<DocumentEntity> original = jpaDocumentRepository.findById(id);
        original.ifPresent(value -> value.setName(document.name()));
        original.ifPresent(value -> value.setType(document.type()));
        original.ifPresent(value -> value.setFilePath(document.filePath()));
        original.ifPresent(value -> value.setHash(document.hash()));
        original.ifPresent(value -> value.setExtracted(document.extracted()));

        if (original.isPresent()) {
            jpaDocumentRepository.save(original.get());
            return documentMapper.toDomain(original.get());
        }

        return new Document();
    }

    @Override
    public void deleteById(UUID id) {
        jpaDocumentRepository.deleteById(id);
    }

    private List<Document> map(List<DocumentEntity> entities) {
        return entities.stream()
                .map(documentMapper::toDomain)
                .toList();
    }
}
