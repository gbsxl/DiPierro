package Di.Pierro.application.port.output;

import Di.Pierro.application.dto.asset.CreateAssetInput;
import Di.Pierro.domain.model.Asset;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetRepository {
    void save(Asset asset, UUID personId);
    Optional<Asset> findById(UUID id);
    List<Asset> findAll();
    List<Asset> findAllByIds(List<UUID> ids);
    List<Asset> findByType(String string);
    List<Asset> findByStillHaveIt(boolean stillHaveIt);
    Asset updateById(UUID id, CreateAssetInput asset);
    void deleteById(UUID id);
}
