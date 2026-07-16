package Di.Pierro.infrastructure.persistence.association;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.AssociationRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Association;
import Di.Pierro.infrastructure.mapper.AssociationMapper;
import Di.Pierro.infrastructure.persistence.entity.AssociationEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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

    private Optional<Actor> getActorById(UUID id){
        return actorRepository.findById(id);
    }
}
