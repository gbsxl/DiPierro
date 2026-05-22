package Di.Pierro.application.usecase.actor;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;
import java.util.Optional;
import java.util.UUID;

public class GetActorByIdUseCase {
    private final ActorRepository actorRepository;

    public GetActorByIdUseCase(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Optional<Actor> execute(UUID id){
        return actorRepository.findById(id);
    }

}
