package Di.Pierro.application.port.input;

import Di.Pierro.domain.entity.Actor;

import java.util.Optional;
import java.util.UUID;

public interface FindActorByIdUseCase {
    Optional<Actor> execute(UUID id);
}
