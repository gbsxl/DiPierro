package Di.Pierro.application.port.usecase.actorindicator;

import Di.Pierro.application.dto.actorindicator.CreateActorIndicatorInput;
import Di.Pierro.application.port.input.ActorIndicatorUseCases;
import Di.Pierro.application.port.output.ActorIndicatorRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.ActorIndicator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ActorIndicatorUseCasesImplementation implements ActorIndicatorUseCases {
    private final ActorIndicatorRepository actorIndicatorRepository;

    public ActorIndicatorUseCasesImplementation(ActorIndicatorRepository actorIndicatorRepository) {
        this.actorIndicatorRepository = actorIndicatorRepository;
    }

    @Override
    public void createActorIndicator(CreateActorIndicatorInput createActorIndicatorInput) {
        ActorIndicator actorIndicator = ActorIndicator.createActorIndicator(
                createActorIndicatorInput.indicatorType(),
                createActorIndicatorInput.value(),
                createActorIndicatorInput.source(),
                createActorIndicatorInput.date(),
                new Actor()
        );
        actorIndicatorRepository.save(actorIndicator, createActorIndicatorInput.actorId());
    }

    @Override
    public List<ActorIndicator> findAll() {
        return actorIndicatorRepository.findAll();
    }

    @Override
    public Optional<ActorIndicator> findById(UUID id) {
        return actorIndicatorRepository.findById(id);
    }
}
