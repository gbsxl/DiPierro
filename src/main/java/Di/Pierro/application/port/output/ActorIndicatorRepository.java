package Di.Pierro.application.port.output;

import Di.Pierro.domain.model.ActorIndicator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActorIndicatorRepository {
    void save(ActorIndicator actorIndicator, UUID actorId);
    Optional<ActorIndicator> findById(UUID id);
    List<ActorIndicator> findAll();
}
