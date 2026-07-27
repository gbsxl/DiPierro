package Di.Pierro.presentation.redflag;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.dto.redflag.RedFlagFilter;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.domain.model.RedFlag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/red-flag")
public class RedFlagController {
    private final RedFlagUseCases redFlagUseCases;

    public RedFlagController(RedFlagUseCases redFlagUseCases) {
        this.redFlagUseCases = redFlagUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreateRedFlagInput redFlagInput) {
        redFlagUseCases.createRedFlag(redFlagInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RedFlag> findById(@PathVariable @NotNull UUID id) {
        Optional<RedFlag> optionalRedFlag = redFlagUseCases.findById(id);
        return optionalRedFlag.map(redFlag -> ResponseEntity.ok(optionalRedFlag.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RedFlag>> findAll() {
        return ResponseEntity.ok(redFlagUseCases.findAll());
    }

    @GetMapping("/{actorId}/actorId")
    public ResponseEntity<List<RedFlag>> findByActorId(@PathVariable @NotNull UUID actorId) {
        return ResponseEntity.ok(redFlagUseCases.findByActorId(actorId));
    }

    @GetMapping("/{publicProcurementId}/publicProcurementId")
    public ResponseEntity<List<RedFlag>> findByPublicProcurementId(@PathVariable @NotNull UUID publicProcurementId) {
        return ResponseEntity.ok(redFlagUseCases.findByPublicProcurementId(publicProcurementId));
    }

    @GetMapping("/{transactionId}/transactionId")
    public ResponseEntity<List<RedFlag>> findByTransactionId(@PathVariable @NotNull UUID transactionId) {
        return ResponseEntity.ok(redFlagUseCases.findByTransactionId(transactionId));
    }

    @GetMapping("/{associationId}/associationId")
    public ResponseEntity<List<RedFlag>> findByAssociationId(@PathVariable @NotNull UUID associationId) {
        return ResponseEntity.ok(redFlagUseCases.findByAssociationId(associationId));
    }

    @GetMapping("/{string}/type")
    public ResponseEntity<List<RedFlag>> findByType(@PathVariable String string) {
        return ResponseEntity.ok(redFlagUseCases.findByType(string));
    }

    @GetMapping("/{severity}/severity")
    public ResponseEntity<List<RedFlag>> findBySeverity(@PathVariable Integer severity) {
        return ResponseEntity.ok(redFlagUseCases.findBySeverity(severity));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RedFlag>> findByFilter(@Valid @ModelAttribute RedFlagFilter filter) {
        return ResponseEntity.ok(redFlagUseCases.findByFilter(filter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RedFlag> update(@PathVariable @NotNull UUID id, @RequestBody CreateRedFlagInput redFlag) {
        return ResponseEntity.ok(redFlagUseCases.updateById(id, redFlag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NotNull UUID id) {
        redFlagUseCases.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
