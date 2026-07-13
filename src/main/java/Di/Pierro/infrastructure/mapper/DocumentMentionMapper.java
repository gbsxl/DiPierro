package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.DocumentMention;
import Di.Pierro.infrastructure.persistence.entity.DocumentMentionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class, ActorMapper.class})
public interface DocumentMentionMapper {
    DocumentMention toDomain(DocumentMentionEntity documentMentionEntity);
    DocumentMentionEntity toEntity(DocumentMention documentMention);
}
