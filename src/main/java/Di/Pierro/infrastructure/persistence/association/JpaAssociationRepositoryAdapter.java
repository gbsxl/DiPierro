package Di.Pierro.infrastructure.persistence.association;

import Di.Pierro.application.dto.association.AssociationFilter;
import Di.Pierro.application.dto.association.CreateAssociationInput;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.AssociationRepository;
import Di.Pierro.domain.enums.AssociationType;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Association;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;
import Di.Pierro.infrastructure.mapper.AssociationMapper;
import Di.Pierro.infrastructure.persistence.entity.AssociationEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaAssociationRepositoryAdapter implements AssociationRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaAssociationRepositoryAdapter.class);

    private final JpaAssociationRepository jpaAssociationRepository;
    private final AssociationMapper associationMapper;
    private final ActorRepository actorRepository;

    @Override
    public void save(Association association, UUID firstActor, UUID secondActor) {
        getActorById(firstActor).ifPresent(association::setFirstActor);
        getActorById(secondActor).ifPresent(association::setSecondActor);
        log.debug("Saving association between actorId={} and actorId={}", firstActor, secondActor);
        jpaAssociationRepository.save(associationMapper.toEntity(association));
    }

    @Override
    public Optional<Association> findById(UUID id) {
        log.debug("Looking up association by id={}", id);
        return jpaAssociationRepository.findById(id).map(associationMapper::toDomain);
    }

    @Override
    public List<Association> findAll() {
        log.debug("Fetching all associations");
        return jpaAssociationRepository.findAll().stream().map(associationMapper::toDomain).toList();
    }

    @Override
    public List<Association> findAllByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        log.debug("Fetching associations by ids list count={}", ids.size());
        return map(jpaAssociationRepository.findAllById(ids));
    }

    @Override
    public List<Association> findByFilter(AssociationFilter filter) {
        log.debug("Searching associations by filter={}", filter);
        Specification<AssociationEntity> spec = Specification.unrestricted();

        if (filter.firstActorId() != null) {
            spec = spec.and(AssociationSpecifications.hasFirstActorId(filter.firstActorId()));
        }
        if (filter.secondActorId() != null) {
            spec = spec.and(AssociationSpecifications.hasSecondActorId(filter.secondActorId()));
        }
        if (filter.firstActorIds() != null && !filter.firstActorIds().isEmpty()) {
            spec = spec.and(AssociationSpecifications.hasFirstActorIds(filter.firstActorIds()));
        }
        if (filter.secondActorIds() != null && !filter.secondActorIds().isEmpty()) {
            spec = spec.and(AssociationSpecifications.hasSecondActorIds(filter.secondActorIds()));
        }
        if (filter.actorId() != null) {
            spec = spec.and(AssociationSpecifications.hasActorId(filter.actorId()));
        }
        if (filter.actorIds() != null && !filter.actorIds().isEmpty()) {
            spec = spec.and(AssociationSpecifications.hasActorIds(filter.actorIds()));
        }
        if (filter.associationType() != null) {
            spec = spec.and(AssociationSpecifications.hasAssociationType(filter.associationType()));
        }
        if (filter.associationTypes() != null && !filter.associationTypes().isEmpty()) {
            spec = spec.and(AssociationSpecifications.hasAssociationTypes(filter.associationTypes()));
        }
        if (filter.source() != null && !filter.source().isBlank()) {
            spec = spec.and(AssociationSpecifications.containsSource(filter.source()));
        }
        if (filter.sources() != null && !filter.sources().isEmpty()) {
            spec = spec.and(AssociationSpecifications.hasSources(filter.sources()));
        }
        if (filter.minimumConfidenceLevel() != null) {
            spec = spec.and(AssociationSpecifications.minimumConfidenceLevel(filter.minimumConfidenceLevel()));
        }
        if (filter.maximumConfidenceLevel() != null) {
            spec = spec.and(AssociationSpecifications.maximumConfidenceLevel(filter.maximumConfidenceLevel()));
        }
        if (filter.associationEnded() != null) {
            spec = spec.and(AssociationSpecifications.isAssociationEnded(filter.associationEnded()));
        }
        if (filter.startAfterDate() != null) {
            spec = spec.and(AssociationSpecifications.startAfterDate(filter.startAfterDate()));
        }
        if (filter.startBeforeDate() != null) {
            spec = spec.and(AssociationSpecifications.startBeforeDate(filter.startBeforeDate()));
        }
        if (filter.endAfterDate() != null) {
            spec = spec.and(AssociationSpecifications.endAfterDate(filter.endAfterDate()));
        }
        if (filter.endBeforeDate() != null) {
            spec = spec.and(AssociationSpecifications.endBeforeDate(filter.endBeforeDate()));
        }

        return map(jpaAssociationRepository.findAll(spec));
    }

    @Override
    public List<Association> findByActorId(UUID id) {
        log.debug("Searching associations by actorId={}", id);
        return map(jpaAssociationRepository.findByFirstActorIdOrSecondActorId(id, id));
    }

    @Override
    public List<Association> findByType(String string) {
        AssociationType type = AssociationType.fromCode(string);
        if (type == AssociationType.LIGACAO_SEM_CLASSIFICACAO) return List.of();
        log.debug("Searching associations by type={}", type);
        return map(jpaAssociationRepository.findByAssociationType(type));
    }

    @Override
    public List<Association> findEndedAssociations() {
        log.debug("Fetching ended associations");
        return map(jpaAssociationRepository.findByAssociationEnded(true));
    }

    @Override
    public Association updateById(UUID id, CreateAssociationInput association) {
        AssociationEntity entity = jpaAssociationRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("association.not-found", "Association", id));

        entity.setAssociationType(AssociationType.fromCode(association.associationType()));
        entity.setSource(association.source());
        entity.setConfidenceLevel(association.confidenceLevel());
        entity.setAssociationEnded(association.associationEnded());
        entity.setAssociationStart(association.associationStart());
        entity.setAssociationEnd(association.associationEnd());
        entity.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        log.debug("Updating association id={}", id);
        jpaAssociationRepository.save(entity);
        return associationMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting association id={}", id);
        jpaAssociationRepository.deleteById(id);
    }

    private Optional<Actor> getActorById(UUID id) {
        return actorRepository.findById(id);
    }

    private List<Association> map(List<AssociationEntity> entities) {
        return entities.stream().map(associationMapper::toDomain).toList();
    }
}
