package Di.Pierro.infrastructure.persistence.actor;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.infrastructure.mapper.ActorMapper;
import Di.Pierro.infrastructure.persistence.entity.ActorEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaActorRepositoryAdapter implements ActorRepository {

    private final JpaActorRepository jpaActorRepository;
    private final ActorMapper actorMapper;

    @Override
    public void save(Actor actor) {
        ActorEntity actorEntity = actorMapper.toEntity(actor);
        jpaActorRepository.save(actorEntity);
    }

    @Override
    public List<Actor> findAll() {
        return jpaActorRepository.findAll().stream().map(actorMapper::toDomain).toList();
    }

    @Override
    public Optional<Actor> findById(UUID id) {
        return jpaActorRepository.findById(id).map(actorMapper::toDomain);
    }
}