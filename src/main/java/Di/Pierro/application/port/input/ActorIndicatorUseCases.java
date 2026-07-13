package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.actorindicator.CreateActorIndicatorInput;
import Di.Pierro.domain.model.ActorIndicator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActorIndicatorUseCases {
    void createActorIndicator(CreateActorIndicatorInput createActorIndicatorInput);
    List<ActorIndicator> findAll();
    Optional<ActorIndicator> findById(UUID id);
}
