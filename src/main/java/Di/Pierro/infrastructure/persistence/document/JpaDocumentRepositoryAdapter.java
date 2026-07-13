package Di.Pierro.infrastructure.persistence.document;

import Di.Pierro.application.port.output.DocumentRepository;
import Di.Pierro.domain.model.Document;
import Di.Pierro.domain.model.PublicProcurement;
import Di.Pierro.infrastructure.mapper.DocumentMapper;
import Di.Pierro.infrastructure.mapper.PublicProcurementMapper;
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
    JpaPublicProcurementRepository jpaPublicProcurementRepository;
    JpaDocumentRepository jpaDocumentRepository;
    DocumentMapper documentMapper;
    PublicProcurementMapper publicProcurementMapper;

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
}
