package Di.Pierro.presentation.association;

import Di.Pierro.application.dto.association.CreateAssociationInput;
import Di.Pierro.application.usecase.association.CreateAssociationUseCase;
import Di.Pierro.application.usecase.association.GetAssociationByIdUseCase;
import Di.Pierro.application.usecase.association.SearchAssociationUseCase;
import Di.Pierro.domain.model.Association;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/association")
public class AssociationController {
    CreateAssociationUseCase createAssociationUseCase;
    GetAssociationByIdUseCase getAssociationByIdUseCase;
    SearchAssociationUseCase searchAssociationUseCase;

    public AssociationController(CreateAssociationUseCase createAssociationUseCase, GetAssociationByIdUseCase getAssociationByIdUseCase, SearchAssociationUseCase searchAssociationUseCase) {
        this.createAssociationUseCase = createAssociationUseCase;
        this.getAssociationByIdUseCase = getAssociationByIdUseCase;
        this.searchAssociationUseCase = searchAssociationUseCase;
    }

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody CreateAssociationInput createAssociationInput){
        createAssociationUseCase.execute(createAssociationInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Association> findById(@PathVariable UUID id){
        Optional<Association> optionalAssociation = getAssociationByIdUseCase.execute(id);
        return optionalAssociation.map(
                association-> ResponseEntity.ok(optionalAssociation.get())
        ).orElseGet( () -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Association>> findAll(){
        return ResponseEntity.ok(searchAssociationUseCase.execute());
    }

}
