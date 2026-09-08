package Di.Pierro.presentation.business;

import Di.Pierro.application.dto.business.CreateBusinessInput;
import Di.Pierro.application.port.input.BusinessUseCases;
import Di.Pierro.domain.model.Business;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreateBusinessInput createBusinessInput) {
        businessUseCases.createBusiness(createBusinessInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Business> findById(@PathVariable @NotNull UUID id) {
        Optional<Business> optionalBusiness = businessUseCases.findById(id);
        return optionalBusiness.map(
                business -> ResponseEntity.ok(optionalBusiness.get())
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Business>> findAll() {
        return ResponseEntity.ok(businessUseCases.findAll());
    }

    @PostMapping("/batch/ids")
    public ResponseEntity<List<Business>> findAllByIds(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(businessUseCases.findAllByIds(ids));
    }

    @GetMapping("/{actorId}/actorId")
    public ResponseEntity<Business> findByActorId(@PathVariable @NotNull UUID actorId) {
        Optional<Business> optionalBusiness = businessUseCases.findByActorId(actorId);
        return optionalBusiness.map(
                business -> ResponseEntity.ok(optionalBusiness.get())
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/batch/actor-ids")
    public ResponseEntity<List<Business>> findByActorIds(@RequestBody List<UUID> actorIds) {
        return ResponseEntity.ok(businessUseCases.findByActorIds(actorIds));
    }

    @GetMapping("/{string}/name")
    public ResponseEntity<List<Business>> findByName(@PathVariable String string) {
        List<Business> businessList = businessUseCases.findByName(string);
        return ResponseEntity.ok(businessList);
    }

    @GetMapping("/{string}/cnpj")
    public ResponseEntity<List<Business>> findByCnpj(@PathVariable String string) {
        List<Business> businessList = businessUseCases.findByCNPJ(string);
        return ResponseEntity.ok(businessList);
    }

    @GetMapping("/{string}/phoneNumber")
    public ResponseEntity<List<Business>> findByPhoneNumber(@PathVariable String string) {
        List<Business> businessList = businessUseCases.findByPhoneNumber(string);
        return ResponseEntity.ok(businessList);
    }

    @GetMapping("/{string}/email")
    public ResponseEntity<List<Business>> findByEmail(@PathVariable String string) {
        List<Business> businessList = businessUseCases.findByEmail(string);
        return ResponseEntity.ok(businessList);
    }

    @GetMapping("/public")
    public ResponseEntity<List<Business>> findPublicCompanies() {
        return ResponseEntity.ok(businessUseCases.findPublicCompanies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Business> update(@PathVariable @NotNull UUID id, @RequestBody @Valid CreateBusinessInput business) {
        return ResponseEntity.ok(businessUseCases.updateById(id, business));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NotNull UUID id) {
        businessUseCases.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
