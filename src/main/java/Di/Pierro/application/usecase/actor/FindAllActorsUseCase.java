package Di.Pierro.application.usecase.actor;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;
import java.util.List;

public class FindAllActorsUseCase {
    private final ActorRepository actorRepository;

    public FindAllActorsUseCase(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<Actor> execute(){
        return actorRepository.getActors();
    }
}
