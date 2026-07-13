package Di.Pierro.presentation.publicprocurement;

import Di.Pierro.application.dto.publicprocurement.CreatePublicProcurementInput;
import Di.Pierro.application.port.input.PublicProcurementUseCases;
import Di.Pierro.domain.model.PublicProcurement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/public-procurement")
public class PublicProcurementController {
    private final PublicProcurementUseCases publicProcurementUseCases;

    public PublicProcurementController(PublicProcurementUseCases publicProcurementUseCases) {
        this.publicProcurementUseCases = publicProcurementUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreatePublicProcurementInput publicProcurementInput) {
        publicProcurementUseCases.createPublicProcurement(publicProcurementInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicProcurement> findById(@PathVariable @NotNull UUID id) {
        Optional<PublicProcurement> optionalPublicProcurement = publicProcurementUseCases.findById(id);
        return optionalPublicProcurement.map(publicProcurement -> ResponseEntity.ok(optionalPublicProcurement.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PublicProcurement>> findAll() {
        return ResponseEntity.ok(publicProcurementUseCases.findAll());
    }
}
