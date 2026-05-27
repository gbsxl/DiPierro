package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.Actor;
import Di.Pierro.infrastructure.persistence.entity.ActorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActorMapper {
    ActorEntity toEntity(Actor actor);
    Actor toDomain(ActorEntity actorEntity);
}
