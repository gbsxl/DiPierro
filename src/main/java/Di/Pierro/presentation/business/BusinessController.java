package Di.Pierro.presentation.business;

import Di.Pierro.application.dto.business.CreateBusinessInput;
import Di.Pierro.application.usecase.business.CreateBusinessUseCase;
import Di.Pierro.application.usecase.business.GetBusinessByIdUseCase;
import Di.Pierro.application.usecase.business.SearchBusinessUseCase;
import Di.Pierro.domain.model.Business;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/business")
public class BusinessController {
    private final CreateBusinessUseCase createBusinessUseCase;
    private final GetBusinessByIdUseCase getBusinessByIdUseCase;
    private final SearchBusinessUseCase searchBusinessUseCase;


    public BusinessController(CreateBusinessUseCase createBusinessUseCase, GetBusinessByIdUseCase getBusinessByIdUseCase, SearchBusinessUseCase searchBusinessUseCase) {
        this.createBusinessUseCase = createBusinessUseCase;
        this.getBusinessByIdUseCase = getBusinessByIdUseCase;
        this.searchBusinessUseCase = searchBusinessUseCase;
    }

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody CreateBusinessInput createBusinessInput){
        createBusinessUseCase.execute(createBusinessInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Business> findById(@PathVariable UUID id){
        Optional<Business> optionalBusiness = getBusinessByIdUseCase.execute(id);
        return optionalBusiness.map(
                business-> ResponseEntity.ok(optionalBusiness.get())
        ).orElseGet( () -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Business>> findAll(){
        return ResponseEntity.ok(searchBusinessUseCase.execute());
    }

}
