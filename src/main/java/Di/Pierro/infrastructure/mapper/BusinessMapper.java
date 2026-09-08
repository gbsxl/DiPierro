package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.Business;
import Di.Pierro.infrastructure.persistence.entity.BusinessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ActorMapper.class, AddressMapper.class})
public interface BusinessMapper {
    Business toDomain(BusinessEntity businessEntity);
    BusinessEntity toEntity(Business business);
}
