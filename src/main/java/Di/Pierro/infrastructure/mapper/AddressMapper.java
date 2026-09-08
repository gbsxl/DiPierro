package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.Address;
import Di.Pierro.infrastructure.persistence.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ActorMapper.class)
public interface AddressMapper {
    @Mapping(target = "actor", ignore = true)
    Address toDomain(AddressEntity entity);

    @Mapping(target = "actor", ignore = true)
    AddressEntity toEntity(Address domain);
}
