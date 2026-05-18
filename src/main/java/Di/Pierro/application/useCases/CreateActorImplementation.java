package Di.Pierro.application.useCases;
import Di.Pierro.application.port.input.CreateActorUseCase;
import Di.Pierro.application.port.output.ActorRepository;
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