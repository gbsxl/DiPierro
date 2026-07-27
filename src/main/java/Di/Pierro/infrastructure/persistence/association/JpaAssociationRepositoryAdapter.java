package Di.Pierro.infrastructure.persistence.association;

import Di.Pierro.application.dto.association.CreateAssociationInput;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.AssociationRepository;
import Di.Pierro.domain.enums.AssociationType;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Association;
import Di.Pierro.infrastructure.mapper.AssociationMapper;
import Di.Pierro.infrastructure.persistence.entity.AssociationEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaAssociationRepositoryAdapter implements AssociationRepository {

    private final JpaAssociationRepository jpaAssociationRepository;
    private final AssociationMapper associationMapper;
    private final ActorRepository actorRepository;

    @Override
    public void save(Association association, UUID firstActor, UUID secondActor) {
        getActorById(firstActor).ifPresent(association::setFirstActor);
        getActorById(secondActor).ifPresent(association::setSecondActor);
        AssociationEntity associationEntity = associationMapper.toEntity(association);
        jpaAssociationRepository.save(associationEntity);
    }

    @Override
    public Optional<Association> findById(UUID id) {
        return jpaAssociationRepository
                .findById(id)
                .map(associationMapper::toDomain);
    }

    @Override
    public List<Association> findAll() {
        return jpaAssociationRepository
                .findAll()
                .stream()
                .map(associationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Association> findByActorId(UUID id) {
        return map(jpaAssociationRepository.findByFirstActorIdOrSecondActorId(id, id));
    }

    @Override
    public List<Association> findByType(String string) {
        AssociationType type = AssociationType.fromCode(string);

        if (type == AssociationType.LIGACAO_SEM_CLASSIFICACAO) return List.of();

        return map(jpaAssociationRepository.findByAssociationType(type));
    }

    @Override
    public List<Association> findEndedAssociations() {
        return map(jpaAssociationRepository.findByAssociationEnded(true));
    }

    @Override
    public Association updateById(UUID id, CreateAssociationInput association) {
        Optional<AssociationEntity> original = jpaAssociationRepository.findById(id);
        original.ifPresent(value -> value.setAssociationType(AssociationType.fromCode(association.associationType())));
        original.ifPresent(value -> value.setSource(association.source()));
        original.ifPresent(value -> value.setConfidenceLevel(association.confidenceLevel()));
        original.ifPresent(value -> value.setAssociationEnded(association.associationEnded()));
        original.ifPresent(value -> value.setAssociationStart(association.associationStart()));
        original.ifPresent(value -> value.setAssociationEnd(association.associationEnd()));
        original.ifPresent(value -> value.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC)));

        if (original.isPresent()) {
            jpaAssociationRepository.save(original.get());
            return associationMapper.toDomain(original.get());
        }

        return new Association();
    }

    @Override
    public void deleteById(UUID id) {
        jpaAssociationRepository.deleteById(id);
    }

    private Optional<Actor> getActorById(UUID id) {
        return actorRepository.findById(id);
    }

    private List<Association> map(List<AssociationEntity> entities) {
        return entities.stream()
                .map(associationMapper::toDomain)
                .toList();
    }
}
