package Di.Pierro.presentation.controller;

import Di.Pierro.application.port.input.CreateActorUseCase;
import Di.Pierro.application.port.input.FindActorByIdUseCase;
import Di.Pierro.application.port.input.FindAllActorsUseCase;
import Di.Pierro.domain.entity.Actor;
import Di.Pierro.presentation.request.ActorRequest;
import Di.Pierro.presentation.response.ActorResponse;
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
    public ResponseEntity<Void> createActor(@RequestBody ActorRequest actorRequest) {
        createActorUseCase.execute(actorRequest.Address());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ActorResponse>> getAllActors() {
        return ResponseEntity.ok(
                findAllActorsUseCase.execute()
                        .stream()
                        .map(
                                actor -> new ActorResponse(
                                        actor.getId(),
                                        actor.getAddress(),
                                        actor.getCreatedAt(),
                                        actor.getUpdatedAt(),
                                        actor.isActive()))
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorResponse> getActorById(@PathVariable UUID id) {
        Optional<Actor> actorOptional = findActorByIdUseCase.execute(id);
        return actorOptional.map(actor -> ResponseEntity.ok(new ActorResponse(
                actor.getId(),
                actor.getAddress(),
                actor.getCreatedAt(),
                actor.getUpdatedAt(),
                actor.isActive()))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
