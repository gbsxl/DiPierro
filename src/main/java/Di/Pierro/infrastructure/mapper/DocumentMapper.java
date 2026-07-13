package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.Document;
import Di.Pierro.infrastructure.persistence.entity.DocumentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PublicProcurementMapper.class)
public interface DocumentMapper {
    Document toDomain(DocumentEntity documentEntity);
    DocumentEntity toEntity(Document document);
}
