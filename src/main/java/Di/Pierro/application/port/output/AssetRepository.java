package Di.Pierro.application.port.output;

import Di.Pierro.domain.model.Asset;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetRepository {
    void save(Asset asset, UUID personId);
    Optional<Asset> findById(UUID id);
    List<Asset> findAll();
}
