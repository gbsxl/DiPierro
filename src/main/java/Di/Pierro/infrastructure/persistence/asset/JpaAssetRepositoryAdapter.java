package Di.Pierro.infrastructure.persistence.asset;

import Di.Pierro.application.dto.asset.CreateAssetInput;
import Di.Pierro.application.port.output.AssetRepository;
import Di.Pierro.domain.model.Asset;
import Di.Pierro.domain.model.Person;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;
import Di.Pierro.infrastructure.mapper.AssetMapper;
import Di.Pierro.infrastructure.mapper.PersonMapper;
import Di.Pierro.infrastructure.persistence.entity.AssetEntity;
import Di.Pierro.infrastructure.persistence.entity.PersonEntity;
import Di.Pierro.infrastructure.persistence.person.JpaPersonRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaAssetRepositoryAdapter implements AssetRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaAssetRepositoryAdapter.class);

    private final JpaPersonRepository jpaPersonRepository;
    private final JpaAssetRepository jpaAssetRepository;
    private final AssetMapper assetMapper;
    private final PersonMapper personMapper;

    @Override
    public void save(Asset asset, UUID personId) {
        PersonEntity personEntity = jpaPersonRepository.findById(personId)
                .orElseThrow(() -> ResourceNotFoundException.of("asset.person-not-found", "Person", personId));
        Person person = personMapper.toDomain(personEntity);
        asset.setPerson(person);
        log.debug("Saving asset for personId={}", personId);
        jpaAssetRepository.save(assetMapper.toEntity(asset));
    }

    @Override
    public Optional<Asset> findById(UUID id) {
        log.debug("Looking up asset by id={}", id);
        return jpaAssetRepository.findById(id).map(assetMapper::toDomain);
    }

    @Override
    public List<Asset> findAll() {
        log.debug("Fetching all assets");
        return jpaAssetRepository.findAll().stream().map(assetMapper::toDomain).toList();
    }

    @Override
    public List<Asset> findAllByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        log.debug("Fetching assets by ids list count={}", ids.size());
        return map(jpaAssetRepository.findAllById(ids));
    }

    @Override
    public List<Asset> findByType(String string) {
        if (string == null || string.isBlank()) {
            return List.of();
        }
        log.debug("Searching assets by type containing '{}'", string.trim());
        return map(jpaAssetRepository.findTop100ByTypeContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Asset> findByStillHaveIt(boolean stillHaveIt) {
        log.debug("Searching assets by stillHaveIt={}", stillHaveIt);
        return map(jpaAssetRepository.findByStillHaveIt(stillHaveIt));
    }

    @Override
    public Asset updateById(UUID id, CreateAssetInput asset) {
        AssetEntity entity = jpaAssetRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("asset.not-found", "Asset", id));

        entity.setType(asset.type());
        entity.setDescription(asset.description());
        entity.setEstimatedValue(asset.estimatedValue());
        entity.setSource(asset.source());
        entity.setStillHaveIt(asset.stillHaveIt());
        entity.setAcquiredAt(asset.acquiredAt());
        entity.setMappedAt(asset.mappedAt());

        log.debug("Updating asset id={}", id);
        jpaAssetRepository.save(entity);
        return assetMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting asset id={}", id);
        jpaAssetRepository.deleteById(id);
    }

    private List<Asset> map(List<AssetEntity> entities) {
        return entities.stream().map(assetMapper::toDomain).toList();
    }
}
