package Di.Pierro.presentation.association;

import Di.Pierro.application.dto.association.AssociationFilter;
import Di.Pierro.application.dto.association.CreateAssociationInput;
import Di.Pierro.application.port.input.AssociationUseCases;
import Di.Pierro.domain.model.Association;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/association")
public class AssociationController {
    private final AssociationUseCases associationUseCases;

    public AssociationController(AssociationUseCases associationUseCases) {
        this.associationUseCases = associationUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreateAssociationInput createAssociationInput) {
        associationUseCases.createAssociation(createAssociationInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Association> findById(@PathVariable @NotNull UUID id) {
        Optional<Association> optionalAssociation = associationUseCases.findById(id);
        return optionalAssociation.map(
                association -> ResponseEntity.ok(optionalAssociation.get())
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Association>> findAll() {
        return ResponseEntity.ok(associationUseCases.findAll());
    }

    @PostMapping("/batch/ids")
    public ResponseEntity<List<Association>> findAllByIds(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(associationUseCases.findAllByIds(ids));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Association>> findByFilter(@Valid @ModelAttribute AssociationFilter filter) {
        return ResponseEntity.ok(associationUseCases.findByFilter(filter));
    }

    @GetMapping("/{actorId}/actorId")
    public ResponseEntity<List<Association>> findByActorId(@PathVariable @NotNull UUID actorId) {
        List<Association> associationList = associationUseCases.findByActorId(actorId);
        return ResponseEntity.ok(associationList);
    }

    @GetMapping("/{string}/type")
    public ResponseEntity<List<Association>> findByType(@PathVariable String string) {
        List<Association> associationList = associationUseCases.findByType(string);
        return ResponseEntity.ok(associationList);
    }

    @GetMapping("/ended")
    public ResponseEntity<List<Association>> findEndedAssociations() {
        return ResponseEntity.ok(associationUseCases.findEndedAssociations());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Association> update(@PathVariable @NotNull UUID id, @RequestBody CreateAssociationInput association) {
        return ResponseEntity.ok(associationUseCases.updateById(id, association));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NotNull UUID id) {
        associationUseCases.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
