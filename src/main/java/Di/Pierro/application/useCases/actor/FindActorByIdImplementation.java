package Di.Pierro.application.useCases.actor;

import Di.Pierro.application.port.input.actor.FindActorByIdUseCase;
import Di.Pierro.application.port.output.actor.ActorRepository;
import Di.Pierro.domain.entity.Actor;
import java.util.Optional;
import java.util.UUID;

public class FindActorByIdImplementation implements FindActorByIdUseCase {
    private final ActorRepository actorRepository;

    public FindActorByIdImplementation(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public Optional<Actor> execute(UUID id){
        return actorRepository.getActorById(id);
    }

}
