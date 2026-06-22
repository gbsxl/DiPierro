package Di.Pierro.application.port.usecase.actor;

import Di.Pierro.application.dto.actor.CreateActorInput;
import Di.Pierro.application.port.input.ActorUseCases;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ActorUseCasesImplementation implements ActorUseCases {
    ActorRepository actorRepository;

    public ActorUseCasesImplementation(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public void createActor(CreateActorInput createActorInput) {
        Actor actor = Actor.createActor(createActorInput.address());
        actorRepository.save(actor);
    }

    @Override
    public List<Actor> findAll() {
        return actorRepository.findAll();
    }

    @Override
    public Optional<Actor> findById(UUID id) {
        return actorRepository.findById(id);
    }
}
