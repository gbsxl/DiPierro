package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.ActorIndicator;
import Di.Pierro.infrastructure.persistence.entity.ActorIndicatorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ActorMapper.class)
public interface ActorIndicatorMapper {
    ActorIndicator toDomain(ActorIndicatorEntity actorIndicatorEntity);
    ActorIndicatorEntity toEntity(ActorIndicator actorIndicator);
}
