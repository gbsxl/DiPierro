package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.RedFlag;
import Di.Pierro.infrastructure.persistence.entity.RedFlagEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ActorMapper.class, PublicProcurementMapper.class})
public interface RedFlagMapper {
    RedFlag toDomain(RedFlagEntity redFlagEntity);
    RedFlagEntity toEntity(RedFlag redFlag);
}
