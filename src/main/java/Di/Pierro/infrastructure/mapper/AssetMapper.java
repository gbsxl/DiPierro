package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.Asset;
import Di.Pierro.infrastructure.persistence.entity.AssetEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PersonMapper.class)
public interface AssetMapper {
    Asset toDomain(AssetEntity assetEntity);
    AssetEntity toEntity(Asset asset);
}
