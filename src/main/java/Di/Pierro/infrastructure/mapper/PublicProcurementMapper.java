package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.PublicProcurement;
import Di.Pierro.infrastructure.persistence.entity.PublicProcurementEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ActorMapper.class)
public interface PublicProcurementMapper {
    PublicProcurement toDomain(PublicProcurementEntity publicProcurementEntity);
    PublicProcurementEntity toEntity(PublicProcurement publicProcurement);
}
