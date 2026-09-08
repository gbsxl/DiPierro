package Di.Pierro.application.port.usecase.asset;

import Di.Pierro.application.dto.asset.CreateAssetInput;
import Di.Pierro.application.port.input.AssetUseCases;
import Di.Pierro.application.port.output.AssetRepository;
import Di.Pierro.domain.model.Asset;
import Di.Pierro.domain.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AssetUseCasesImplementation implements AssetUseCases {
    private final AssetRepository assetRepository;

    public AssetUseCasesImplementation(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public void createAsset(CreateAssetInput createAssetInput) {
        Asset asset = Asset.createAsset(
                createAssetInput.type(),
                createAssetInput.description(),
                createAssetInput.estimatedValue(),
                createAssetInput.source(),
                createAssetInput.stillHaveIt(),
                createAssetInput.acquiredAt(),
                createAssetInput.mappedAt(),
                new Person()
        );
        assetRepository.save(asset, createAssetInput.personId());
    }

    @Override
    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    @Override
    public List<Asset> findAllByIds(List<UUID> ids) {
        return assetRepository.findAllByIds(ids);
    }

    @Override
    public Optional<Asset> findById(UUID id) {
        return assetRepository.findById(id);
    }

    @Override
    public List<Asset> findByType(String string) {
        return assetRepository.findByType(string);
    }

    @Override
    public List<Asset> findByStillHaveIt(boolean stillHaveIt) {
        return assetRepository.findByStillHaveIt(stillHaveIt);
    }

    @Override
    public Asset updateById(UUID id, CreateAssetInput asset) {
        return assetRepository.updateById(id, asset);
    }

    @Override
    public void deleteById(UUID id) {
        assetRepository.deleteById(id);
    }
}
