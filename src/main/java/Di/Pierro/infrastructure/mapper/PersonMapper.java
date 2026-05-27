package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.Person;
import Di.Pierro.infrastructure.persistence.entity.PersonEntity;
import lombok.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ActorMapper.class)
public interface PersonMapper {
    Person toDomain(PersonEntity personEntity);
    PersonEntity toEntity(Person person);
}
