package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.Association;
import Di.Pierro.infrastructure.persistence.entity.AssociationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ActorMapper.class)
public interface AssociationMapper {
    AssociationEntity toEntity(Association association);
    Association toDomain(AssociationEntity associationEntity);
}
