package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.asset.CreateAssetInput;
import Di.Pierro.domain.model.Asset;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetUseCases {
    void createAsset(CreateAssetInput createAssetInput);
    List<Asset> findAll();
    Optional<Asset> findById(UUID id);
    List<Asset> findByType(String string);
    List<Asset> findByStillHaveIt(boolean stillHaveIt);
    Asset updateById(UUID id, CreateAssetInput asset);
    void deleteById(UUID id);
}
