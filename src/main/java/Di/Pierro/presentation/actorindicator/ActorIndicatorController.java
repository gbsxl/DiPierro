package Di.Pierro.presentation.actorindicator;

import Di.Pierro.application.dto.actorindicator.CreateActorIndicatorInput;
import Di.Pierro.application.port.input.ActorIndicatorUseCases;
import Di.Pierro.domain.model.ActorIndicator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/actor-indicator")
public class ActorIndicatorController {
    private final ActorIndicatorUseCases actorIndicatorUseCases;

    public ActorIndicatorController(ActorIndicatorUseCases actorIndicatorUseCases) {
        this.actorIndicatorUseCases = actorIndicatorUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreateActorIndicatorInput actorIndicatorInput) {
        actorIndicatorUseCases.createActorIndicator(actorIndicatorInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorIndicator> findById(@PathVariable @NotNull UUID id) {
        Optional<ActorIndicator> optionalActorIndicator = actorIndicatorUseCases.findById(id);
        return optionalActorIndicator.map(actorIndicator -> ResponseEntity.ok(optionalActorIndicator.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ActorIndicator>> findAll() {
        return ResponseEntity.ok(actorIndicatorUseCases.findAll());
    }
}
