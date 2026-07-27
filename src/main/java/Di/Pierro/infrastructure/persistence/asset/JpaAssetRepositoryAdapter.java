package Di.Pierro.infrastructure.persistence.asset;

import Di.Pierro.application.dto.asset.CreateAssetInput;
import Di.Pierro.application.port.output.AssetRepository;
import Di.Pierro.domain.model.Asset;
import Di.Pierro.domain.model.Person;
import Di.Pierro.infrastructure.mapper.AssetMapper;
import Di.Pierro.infrastructure.mapper.PersonMapper;
import Di.Pierro.infrastructure.persistence.entity.AssetEntity;
import Di.Pierro.infrastructure.persistence.entity.PersonEntity;
import Di.Pierro.infrastructure.persistence.person.JpaPersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaAssetRepositoryAdapter implements AssetRepository {
    private final JpaPersonRepository jpaPersonRepository;
    private final JpaAssetRepository jpaAssetRepository;
    private final AssetMapper assetMapper;
    private final PersonMapper personMapper;

    @Override
    public void save(Asset asset, UUID personId) {
        Optional<PersonEntity> optionalPerson = jpaPersonRepository.findById(personId);
        if (optionalPerson.isPresent()) {
            Person person = personMapper.toDomain(optionalPerson.get());
            asset.setPerson(person);
            jpaAssetRepository.save(assetMapper.toEntity(asset));
        }
    }

    @Override
    public Optional<Asset> findById(UUID id) {
        return jpaAssetRepository.findById(id).map(assetMapper::toDomain);
    }

    @Override
    public List<Asset> findAll() {
        return jpaAssetRepository.findAll().stream().map(assetMapper::toDomain).toList();
    }

    @Override
    public List<Asset> findByType(String string) {
        if (string == null || string.isBlank()) {
            return List.of();
        }

        return map(jpaAssetRepository.findTop100ByTypeContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Asset> findByStillHaveIt(boolean stillHaveIt) {
        return map(jpaAssetRepository.findByStillHaveIt(stillHaveIt));
    }

    @Override
    public Asset updateById(UUID id, CreateAssetInput asset) {
        Optional<AssetEntity> original = jpaAssetRepository.findById(id);
        original.ifPresent(value -> value.setType(asset.type()));
        original.ifPresent(value -> value.setDescription(asset.description()));
        original.ifPresent(value -> value.setEstimatedValue(asset.estimatedValue()));
        original.ifPresent(value -> value.setSource(asset.source()));
        original.ifPresent(value -> value.setStillHaveIt(asset.stillHaveIt()));
        original.ifPresent(value -> value.setAcquiredAt(asset.acquiredAt()));
        original.ifPresent(value -> value.setMappedAt(asset.mappedAt()));

        if (original.isPresent()) {
            jpaAssetRepository.save(original.get());
            return assetMapper.toDomain(original.get());
        }

        return new Asset();
    }

    @Override
    public void deleteById(UUID id) {
        jpaAssetRepository.deleteById(id);
    }

    private List<Asset> map(List<AssetEntity> entities) {
        return entities.stream()
                .map(assetMapper::toDomain)
                .toList();
    }
}
