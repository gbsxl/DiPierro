package Di.Pierro.infrastructure.persistence.asset;

import Di.Pierro.application.port.output.AssetRepository;
import Di.Pierro.domain.model.Asset;
import Di.Pierro.domain.model.Person;
import Di.Pierro.infrastructure.mapper.AssetMapper;
import Di.Pierro.infrastructure.mapper.PersonMapper;
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
    JpaPersonRepository jpaPersonRepository;
    JpaAssetRepository jpaAssetRepository;
    AssetMapper assetMapper;
    PersonMapper personMapper;

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
}
