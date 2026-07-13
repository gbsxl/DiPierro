package Di.Pierro.infrastructure.persistence.actorindicator;

import Di.Pierro.application.port.output.ActorIndicatorRepository;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.ActorIndicator;
import Di.Pierro.infrastructure.mapper.ActorIndicatorMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaActorIndicatorRepositoryAdapter implements ActorIndicatorRepository {
    ActorRepository actorRepository;
    JpaActorIndicatorRepository jpaActorIndicatorRepository;
    ActorIndicatorMapper actorIndicatorMapper;

    @Override
    public void save(ActorIndicator actorIndicator, UUID actorId) {
        Optional<Actor> optionalActor = actorRepository.findById(actorId);
        if (optionalActor.isPresent()) {
            actorIndicator.setActor(optionalActor.get());
            jpaActorIndicatorRepository.save(actorIndicatorMapper.toEntity(actorIndicator));
        }
    }

    @Override
    public Optional<ActorIndicator> findById(UUID id) {
        return jpaActorIndicatorRepository.findById(id).map(actorIndicatorMapper::toDomain);
    }

    @Override
    public List<ActorIndicator> findAll() {
        return jpaActorIndicatorRepository.findAll().stream().map(actorIndicatorMapper::toDomain).toList();
    }
}
