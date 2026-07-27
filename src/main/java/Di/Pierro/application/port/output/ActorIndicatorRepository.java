package Di.Pierro.application.port.output;

import Di.Pierro.application.dto.actorindicator.CreateActorIndicatorInput;
import Di.Pierro.domain.model.ActorIndicator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActorIndicatorRepository {
    void save(ActorIndicator actorIndicator, UUID actorId);
    Optional<ActorIndicator> findById(UUID id);
    List<ActorIndicator> findAll();
    List<ActorIndicator> findByActorId(UUID id);
    List<ActorIndicator> findByIndicatorType(String string);
    ActorIndicator updateById(UUID id, CreateActorIndicatorInput actorIndicator);
    void deleteById(UUID id);
}
