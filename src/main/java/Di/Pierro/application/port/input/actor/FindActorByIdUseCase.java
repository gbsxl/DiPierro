package Di.Pierro.application.port.input.actor;

import Di.Pierro.domain.model.Actor;

import java.util.Optional;
import java.util.UUID;

public interface FindActorByIdUseCase {
    Optional<Actor> execute(UUID id);
}
