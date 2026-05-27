package Di.Pierro.application.usecase.actor;

import Di.Pierro.application.dto.actor.CreateActorInput;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;

public class CreateActorUseCase {
    private final ActorRepository actorRepository;

    public CreateActorUseCase(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public void execute(CreateActorInput createActorInput){
        Actor actor = Actor.createActor(createActorInput.address());
        actorRepository.save(actor);
    }
}