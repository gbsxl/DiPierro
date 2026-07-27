package Di.Pierro.application.port.input;

import Di.Pierro.domain.model.Actor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActorUseCases {
    Actor createActor();
    List<Actor> findAll();
    Optional<Actor> findById(UUID id);
}
