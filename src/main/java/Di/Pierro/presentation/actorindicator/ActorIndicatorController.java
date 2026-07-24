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

    @GetMapping("/{actorId}/actorId")
    public ResponseEntity<List<ActorIndicator>> findByActorId(@PathVariable @NotNull UUID actorId) {
        return ResponseEntity.ok(actorIndicatorUseCases.findByActorId(actorId));
    }

    @GetMapping("/{string}/indicatorType")
    public ResponseEntity<List<ActorIndicator>> findByIndicatorType(@PathVariable String string) {
        return ResponseEntity.ok(actorIndicatorUseCases.findByIndicatorType(string));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorIndicator> update(@PathVariable @NotNull UUID id, @RequestBody CreateActorIndicatorInput actorIndicator) {
        return ResponseEntity.ok(actorIndicatorUseCases.updateById(id, actorIndicator));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NotNull UUID id) {
        actorIndicatorUseCases.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
