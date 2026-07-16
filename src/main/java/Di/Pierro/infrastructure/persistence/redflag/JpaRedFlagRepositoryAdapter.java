package Di.Pierro.infrastructure.persistence.redflag;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.RedFlagRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.RedFlag;
import Di.Pierro.infrastructure.mapper.PublicProcurementMapper;
import Di.Pierro.infrastructure.mapper.RedFlagMapper;
import Di.Pierro.infrastructure.persistence.entity.PublicProcurementEntity;
import Di.Pierro.infrastructure.persistence.publicprocurement.JpaPublicProcurementRepository;
import lombok.AllArgsConstructor;
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
}
