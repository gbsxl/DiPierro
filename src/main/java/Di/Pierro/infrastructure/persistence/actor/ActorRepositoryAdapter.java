package Di.Pierro.infrastructure.persistence.actor;

import Di.Pierro.application.port.output.actor.ActorRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.infrastructure.entity.ActorEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ActorRepositoryAdapter implements ActorRepository {

    private final JpaActorRepository jpaActorRepository;

    public ActorRepositoryAdapter(JpaActorRepository jpaActorRepository) {
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
    public List<Actor> getActors() {
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
    public Optional<Actor> getActorById(UUID id) {
        return jpaActorRepository.findById(id).map(
                actorEntity -> new Actor(
                        actorEntity.getId(),
                        actorEntity.getAddress(),
                        actorEntity.getCreatedAt(),
                        actorEntity.getUpdatedAt(),
                        actorEntity.isActive()));
    }
}