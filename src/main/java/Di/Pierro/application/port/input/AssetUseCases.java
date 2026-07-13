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
}
