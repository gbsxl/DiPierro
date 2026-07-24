package Di.Pierro.infrastructure.persistence.redflag;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.dto.redflag.RedFlagFilter;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.RedFlagRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.RedFlag;
import Di.Pierro.infrastructure.mapper.PublicProcurementMapper;
import Di.Pierro.infrastructure.mapper.RedFlagMapper;
import Di.Pierro.infrastructure.persistence.entity.PublicProcurementEntity;
import Di.Pierro.infrastructure.persistence.entity.RedFlagEntity;
import Di.Pierro.infrastructure.persistence.publicprocurement.JpaPublicProcurementRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaRedFlagRepositoryAdapter implements RedFlagRepository {
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
        jpaRedFlagRepository.save(redFlagMapper.toEntity(redFlag));
    }

    @Override
    public Optional<RedFlag> findById(UUID id) {
        return jpaRedFlagRepository.findById(id).map(redFlagMapper::toDomain);
    }

    @Override
    public List<RedFlag> findAll() {
        return jpaRedFlagRepository.findAll().stream().map(redFlagMapper::toDomain).toList();
    }

    @Override
    public List<RedFlag> findByActorId(UUID id) {
        return map(jpaRedFlagRepository.findAll(
                RedFlagSpecifications.hasActor(id)));
    }

    @Override
    public List<RedFlag> findByPublicProcurementId(UUID id) {
        return map(jpaRedFlagRepository.findAll(
                RedFlagSpecifications.hasPublicProcurement(id)));
    }

    @Override
    public List<RedFlag> findByTransactionId(UUID id) {
        return map(jpaRedFlagRepository.findAll(
                RedFlagSpecifications.hasTransaction(id)));
    }

    @Override
    public List<RedFlag> findByAssociationId(UUID id) {
        return map(jpaRedFlagRepository.findAll(
                RedFlagSpecifications.hasAssociation(id)));
    }

    @Override
    public List<RedFlag> findByType(String string) {
        if (string == null || string.isBlank()) {
            return List.of();
        }

        return map(jpaRedFlagRepository.findAll(
                RedFlagSpecifications.hasType(string.trim())));
    }

    @Override
    public List<RedFlag> findBySeverity(Integer severity) {
        return map(jpaRedFlagRepository.findAll(
                RedFlagSpecifications.hasSeverity(severity)));
    }

    @Override
    public List<RedFlag> findByFilter(RedFlagFilter filter) {
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
        Optional<RedFlagEntity> original = jpaRedFlagRepository.findById(id);
        original.ifPresent(value -> value.setType(redFlag.type()));
        original.ifPresent(value -> value.setSeverity(redFlag.severity()));
        original.ifPresent(value -> value.setDescription(redFlag.description()));
        original.ifPresent(value -> value.setTransactionId(redFlag.transactionId()));
        original.ifPresent(value -> value.setAssociationId(redFlag.associationId()));

        if (original.isPresent()) {
            jpaRedFlagRepository.save(original.get());
            return redFlagMapper.toDomain(original.get());
        }

        return new RedFlag();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRedFlagRepository.deleteById(id);
    }

    private List<RedFlag> map(List<RedFlagEntity> entities) {
        return entities.stream()
                .map(redFlagMapper::toDomain)
                .toList();
    }
}
