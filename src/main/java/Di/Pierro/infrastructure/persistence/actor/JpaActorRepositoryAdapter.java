package Di.Pierro.infrastructure.persistence.actor;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.infrastructure.persistence.entity.ActorEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaActorRepositoryAdapter implements ActorRepository {

    private final JpaActorRepository jpaActorRepository;

    public JpaActorRepositoryAdapter(JpaActorRepository jpaActorRepository) {
        this.jpaActorRepository = jpaActorRepository;
    }

    @Override
    public void save(Actor actor) {
        ActorEntity actorEntity = new ActorEntity(
                actor.getId(),
                actor.getAddress(),
                actor.getCreatedAt(),
                actor.getUpdatedAt(),
                actor.isActive()
        );
        jpaActorRepository.save(actorEntity);
    }

    @Override
    public List<Actor> findAll() {
        return jpaActorRepository
                .findAll()
                .stream()
                .map(actorEntity -> new Actor(
                        actorEntity.getId(),
                        actorEntity.getAddress(),
                        actorEntity.getCreatedAt(),
                        actorEntity.getUpdatedAt(),
                        actorEntity.isActive()))
                .toList();
    }

    @Override
    public Optional<Actor> findById(UUID id) {
        return jpaActorRepository.findById(id).map(
                actorEntity -> new Actor(
                        actorEntity.getId(),
                        actorEntity.getAddress(),
                        actorEntity.getCreatedAt(),
                        actorEntity.getUpdatedAt(),
                        actorEntity.isActive()));
    }
}