package Di.Pierro.presentation.actor;

import Di.Pierro.application.port.input.actor.CreateActorUseCase;
import Di.Pierro.application.port.input.actor.FindActorByIdUseCase;
import Di.Pierro.application.port.input.actor.FindAllActorsUseCase;
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
    private final FindAllActorsUseCase findAllActorsUseCase;
    private final FindActorByIdUseCase findActorByIdUseCase;

    public ActorController(CreateActorUseCase createActorUseCase, FindAllActorsUseCase findAllActorsUseCase,
            FindActorByIdUseCase findActorByIdUseCase) {
        this.createActorUseCase = createActorUseCase;
        this.findAllActorsUseCase = findAllActorsUseCase;
        this.findActorByIdUseCase = findActorByIdUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreateActorRequest actorRequest) {
        createActorUseCase.execute(actorRequest.Address());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ActorFoundResponse>> findAll() {
        return ResponseEntity.ok(
                findAllActorsUseCase.execute()
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
        Optional<Actor> actorOptional = findActorByIdUseCase.execute(id);
        return actorOptional.map(actor -> ResponseEntity.ok(new ActorFoundResponse(
                actor.getId(),
                actor.getAddress(),
                actor.getCreatedAt(),
                actor.getUpdatedAt(),
                actor.isActive()))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
