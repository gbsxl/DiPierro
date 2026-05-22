package Di.Pierro.application.usecase.actor;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;
import java.util.List;

public class SearchActorUseCase {
    private final ActorRepository actorRepository;

    public SearchActorUseCase(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<Actor> execute(){
        return actorRepository.findAll();
    }
}
