package Di.Pierro.presentation.actor;

import Di.Pierro.application.dto.actor.CreateActorInput;
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
    public ResponseEntity<Void> save(@RequestBody CreateActorInput actorInput) {
        createActorUseCase.execute(actorInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Actor>> findAll() {
        return ResponseEntity.ok(searchActorUseCase.execute());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> findById(@PathVariable UUID id) {
        Optional<Actor> actorOptional = getActorByIdUseCase.execute(id);
        return actorOptional.map( person -> ResponseEntity.ok(actorOptional.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
