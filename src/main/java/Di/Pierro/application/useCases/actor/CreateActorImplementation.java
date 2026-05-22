package Di.Pierro.application.useCases.actor;
import Di.Pierro.application.port.input.actor.CreateActorUseCase;
import Di.Pierro.application.port.output.actor.ActorRepository;
import Di.Pierro.domain.entity.Actor;

public class CreateActorImplementation implements CreateActorUseCase {
    private final ActorRepository actorRepository;

    public CreateActorImplementation(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public void execute(String address){
        Actor actor = new Actor(address);
        actorRepository.save(actor);
    }
}