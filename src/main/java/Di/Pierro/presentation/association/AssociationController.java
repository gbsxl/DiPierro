package Di.Pierro.presentation.association;

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

    @PostMapping()
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
}
