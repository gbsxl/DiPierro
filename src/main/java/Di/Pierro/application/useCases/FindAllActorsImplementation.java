package Di.Pierro.application.useCases;

import Di.Pierro.application.port.input.FindAllActorsUseCase;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.entity.Actor;
import java.util.List;

public class FindAllActorsImplementation implements FindAllActorsUseCase {
    private final ActorRepository actorRepository;

    public FindAllActorsImplementation(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public List<Actor> execute(){
        return actorRepository.getActors();
    }
}
