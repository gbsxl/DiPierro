package Di.Pierro.presentation.business;

import Di.Pierro.application.dto.business.CreateBusinessInput;
import Di.Pierro.application.port.input.BusinessUseCases;
import Di.Pierro.domain.model.Business;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/business")
public class BusinessController {
    private final BusinessUseCases businessUseCases;

    public BusinessController(BusinessUseCases businessUseCases) {
        this.businessUseCases = businessUseCases;
    }

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody CreateBusinessInput createBusinessInput) {
        businessUseCases.createBusiness(createBusinessInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Business> findById(@PathVariable UUID id) {
        Optional<Business> optionalBusiness = businessUseCases.findById(id);
        return optionalBusiness.map(
                business -> ResponseEntity.ok(optionalBusiness.get())
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Business>> findAll() {
        return ResponseEntity.ok(businessUseCases.findAll());
    }
}
