package Di.Pierro.infrastructure.persistence.redflag;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.dto.redflag.RedFlagFilter;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.RedFlagRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.RedFlag;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;
import Di.Pierro.infrastructure.mapper.PublicProcurementMapper;
import Di.Pierro.infrastructure.mapper.RedFlagMapper;
import Di.Pierro.infrastructure.persistence.entity.PublicProcurementEntity;
import Di.Pierro.infrastructure.persistence.entity.RedFlagEntity;
import Di.Pierro.infrastructure.persistence.publicprocurement.JpaPublicProcurementRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaRedFlagRepositoryAdapter implements RedFlagRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaRedFlagRepositoryAdapter.class);

    private final ActorRepository actorRepository;
    private final JpaPublicProcurementRepository jpaPublicProcurementRepository;
    private final JpaRedFlagRepository jpaRedFlagRepository;
    private final RedFlagMapper redFlagMapper;
    private final PublicProcurementMapper publicProcurementMapper;

    @Override
    public void save(RedFlag redFlag, UUID actorId, UUID publicProcurementId) {
        if (actorId != null) {
            Optional<Actor> optionalActor = actorRepository.findById(actorId);
            optionalActor.ifPresent(redFlag::setActor);
        }
        if (publicProcurementId != null) {
            Optional<PublicProcurementEntity> optionalProcurement = jpaPublicProcurementRepository.findById(publicProcurementId);
            optionalProcurement.map(publicProcurementMapper::toDomain).ifPresent(redFlag::setPublicProcurement);
        }
        log.debug("Saving red flag actorId={} publicProcurementId={}", actorId, publicProcurementId);
        jpaRedFlagRepository.save(redFlagMapper.toEntity(redFlag));
    }

    @Override
    public Optional<RedFlag> findById(UUID id) {
        log.debug("Looking up red flag by id={}", id);
        return jpaRedFlagRepository.findById(id).map(redFlagMapper::toDomain);
    }

    @Override
    public List<RedFlag> findAll() {
        log.debug("Fetching all red flags");
        return jpaRedFlagRepository.findAll().stream().map(redFlagMapper::toDomain).toList();
    }

    @Override
    public List<RedFlag> findByActorId(UUID id) {
        log.debug("Searching red flags by actorId={}", id);
        return map(jpaRedFlagRepository.findAll(RedFlagSpecifications.hasActor(id)));
    }

    @Override
    public List<RedFlag> findByPublicProcurementId(UUID id) {
        log.debug("Searching red flags by publicProcurementId={}", id);
        return map(jpaRedFlagRepository.findAll(RedFlagSpecifications.hasPublicProcurement(id)));
    }

    @Override
    public List<RedFlag> findByTransactionId(UUID id) {
        log.debug("Searching red flags by transactionId={}", id);
        return map(jpaRedFlagRepository.findAll(RedFlagSpecifications.hasTransaction(id)));
    }

    @Override
    public List<RedFlag> findByAssociationId(UUID id) {
        log.debug("Searching red flags by associationId={}", id);
        return map(jpaRedFlagRepository.findAll(RedFlagSpecifications.hasAssociation(id)));
    }

    @Override
    public List<RedFlag> findByType(String string) {
        if (string == null || string.isBlank()) {
            return List.of();
        }
        log.debug("Searching red flags by type='{}'", string.trim());
        return map(jpaRedFlagRepository.findAll(RedFlagSpecifications.hasType(string.trim())));
    }

    @Override
    public List<RedFlag> findBySeverity(Integer severity) {
        log.debug("Searching red flags by severity={}", severity);
        return map(jpaRedFlagRepository.findAll(RedFlagSpecifications.hasSeverity(severity)));
    }

    @Override
    public List<RedFlag> findByFilter(RedFlagFilter filter) {
        log.debug("Searching red flags by filter={}", filter);
        Specification<RedFlagEntity> spec = Specification.unrestricted();

        if (filter.actorId() != null) {
            spec = spec.and(RedFlagSpecifications.hasActor(filter.actorId()));
        }
        if (filter.publicProcurementId() != null) {
            spec = spec.and(RedFlagSpecifications.hasPublicProcurement(filter.publicProcurementId()));
        }
        if (filter.transactionId() != null) {
            spec = spec.and(RedFlagSpecifications.hasTransaction(filter.transactionId()));
        }
        if (filter.associationId() != null) {
            spec = spec.and(RedFlagSpecifications.hasAssociation(filter.associationId()));
        }
        if (filter.type() != null && !filter.type().isBlank()) {
            spec = spec.and(RedFlagSpecifications.hasType(filter.type().trim()));
        }
        if (filter.severity() != null) {
            spec = spec.and(RedFlagSpecifications.hasSeverity(filter.severity()));
        }

        return jpaRedFlagRepository.findAll(spec).stream().map(redFlagMapper::toDomain).toList();
    }

    @Override
    public RedFlag updateById(UUID id, CreateRedFlagInput redFlag) {
        RedFlagEntity entity = jpaRedFlagRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("red-flag.not-found", "RedFlag", id));

        entity.setType(redFlag.type());
        entity.setSeverity(redFlag.severity());
        entity.setDescription(redFlag.description());
        entity.setTransactionId(redFlag.transactionId());
        entity.setAssociationId(redFlag.associationId());

        log.debug("Updating red flag id={}", id);
        jpaRedFlagRepository.save(entity);
        return redFlagMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting red flag id={}", id);
        jpaRedFlagRepository.deleteById(id);
    }

    private List<RedFlag> map(List<RedFlagEntity> entities) {
        return entities.stream().map(redFlagMapper::toDomain).toList();
    }
}
