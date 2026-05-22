package Di.Pierro.presentation.actor;

import Di.Pierro.application.usecase.actor.CreateActorUseCase;
import Di.Pierro.application.usecase.actor.GetActorByIdUseCase;
import Di.Pierro.application.usecase.actor.SearchActorUseCase;
import Di.Pierro.domain.model.Actor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/actor")
public class ActorController {
    private final CreateActorUseCase createActorUseCase;
    private final SearchActorUseCase searchActorUseCase;
    private final GetActorByIdUseCase getActorByIdUseCase;

    public ActorController(CreateActorUseCase createActorUseCase, SearchActorUseCase searchActorUseCase, GetActorByIdUseCase getActorByIdUseCase) {
        this.createActorUseCase = createActorUseCase;
        this.searchActorUseCase = searchActorUseCase;
        this.getActorByIdUseCase = getActorByIdUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreateActorRequest actorRequest) {
        createActorUseCase.execute(actorRequest.Address());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ActorFoundResponse>> findAll() {
        return ResponseEntity.ok(
                searchActorUseCase.execute()
                        .stream()
                        .map(
                                actor -> new ActorFoundResponse(
                                        actor.getId(),
                                        actor.getAddress(),
                                        actor.getCreatedAt(),
                                        actor.getUpdatedAt(),
                                        actor.isActive()))
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorFoundResponse> findById(@PathVariable UUID id) {
        Optional<Actor> actorOptional = getActorByIdUseCase.execute(id);
        return actorOptional.map(actor -> ResponseEntity.ok(new ActorFoundResponse(
                actor.getId(),
                actor.getAddress(),
                actor.getCreatedAt(),
                actor.getUpdatedAt(),
                actor.isActive()))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
