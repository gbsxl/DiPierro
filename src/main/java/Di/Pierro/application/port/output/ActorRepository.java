package Di.Pierro.application.port.output;

import Di.Pierro.domain.entity.Actor;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActorRepository {
    void save(Actor actor);

    List<Actor> getActors();

    Optional<Actor> getActorById(UUID id);
}
