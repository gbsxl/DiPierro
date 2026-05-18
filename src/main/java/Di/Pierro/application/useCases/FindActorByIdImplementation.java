package Di.Pierro.application.useCases;

import Di.Pierro.application.port.input.FindActorByIdUseCase;
import Di.Pierro.application.port.output.ActorRepository;
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
