package Di.Pierro.infrastructure.persistence.publicprocurement;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.PublicProcurementRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.PublicProcurement;
import Di.Pierro.infrastructure.mapper.PublicProcurementMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaPublicProcurementRepositoryAdapter implements PublicProcurementRepository {
    ActorRepository actorRepository;
    JpaPublicProcurementRepository jpaPublicProcurementRepository;
    PublicProcurementMapper publicProcurementMapper;

    @Override
    public void save(PublicProcurement publicProcurement, UUID actorId) {
        Optional<Actor> optionalActor = findActorById(actorId);
        if (optionalActor.isPresent()) {
            publicProcurement.setActor(optionalActor.get());
            jpaPublicProcurementRepository.save(publicProcurementMapper.toEntity(publicProcurement));
        }
    }

    @Override
    public Optional<PublicProcurement> findById(UUID id) {
        return jpaPublicProcurementRepository.findById(id).map(publicProcurementMapper::toDomain);
    }

    @Override
    public List<PublicProcurement> findAll() {
        return jpaPublicProcurementRepository.findAll().stream().map(publicProcurementMapper::toDomain).toList();
    }

    private Optional<Actor> findActorById(UUID id) {
        return actorRepository.findById(id);
    }
}
