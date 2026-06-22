package Di.Pierro.presentation.actor;

import Di.Pierro.application.dto.actor.CreateActorInput;
import Di.Pierro.application.port.input.ActorUseCases;
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
    private final ActorUseCases actorUseCases;

    public ActorController(ActorUseCases actorUseCases) {
        this.actorUseCases = actorUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreateActorInput actorInput) {
        actorUseCases.createActor(actorInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Actor>> findAll() {
        return ResponseEntity.ok(actorUseCases.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> findById(@PathVariable UUID id) {
        Optional<Actor> actorOptional = actorUseCases.findById(id);
        return actorOptional.map( person -> ResponseEntity.ok(actorOptional.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
