package Di.Pierro.application.usecase.actor;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;
import java.util.Optional;
import java.util.UUID;

public class FindActorByIdUseCase {
    private final ActorRepository actorRepository;

    public FindActorByIdUseCase(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Optional<Actor> execute(UUID id){
        return actorRepository.getActorById(id);
    }

}
