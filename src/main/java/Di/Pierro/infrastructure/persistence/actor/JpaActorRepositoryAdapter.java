package Di.Pierro.infrastructure.persistence.actor;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.infrastructure.mapper.ActorMapper;
import Di.Pierro.infrastructure.persistence.entity.ActorEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaActorRepositoryAdapter implements ActorRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaActorRepositoryAdapter.class);

    private final JpaActorRepository jpaActorRepository;
    private final ActorMapper actorMapper;

    @Override
    public void save(Actor actor) {
        log.debug("Saving actor");
        ActorEntity actorEntity = actorMapper.toEntity(actor);
        jpaActorRepository.save(actorEntity);
    }

    @Override
    public List<Actor> findAll() {
        log.debug("Fetching all actors");
        return jpaActorRepository.findAll().stream().map(actorMapper::toDomain).toList();
    }

    @Override
    public Optional<Actor> findById(UUID id) {
        log.debug("Looking up actor by id={}", id);
        return jpaActorRepository.findById(id).map(actorMapper::toDomain);
    }
}