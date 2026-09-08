package Di.Pierro.presentation.transaction;

import Di.Pierro.application.dto.transaction.CreateTransactionInput;
import Di.Pierro.application.dto.transaction.TransactionFilter;
import Di.Pierro.application.port.input.TransactionUseCases;
import Di.Pierro.domain.model.Transaction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionUseCases transactionUseCases;

    public TransactionController(TransactionUseCases transactionUseCases) {
        this.transactionUseCases = transactionUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreateTransactionInput createTransactionInput) {
        transactionUseCases.createTransaction(createTransactionInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> findById(@PathVariable @NotNull UUID id) {
        Optional<Transaction> optionalTransaction = transactionUseCases.findById(id);
        return optionalTransaction.map(
                transaction -> ResponseEntity.ok(optionalTransaction.get())
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> findAll() {
        return ResponseEntity.ok(transactionUseCases.findAll());
    }

    @PostMapping("/batch/ids")
    public ResponseEntity<List<Transaction>> findAllByIds(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(transactionUseCases.findAllByIds(ids));
    }


    @GetMapping("/filter")
    public ResponseEntity<List<Transaction>> findByFilter(@Valid @ModelAttribute TransactionFilter filter) {
        return ResponseEntity.ok(transactionUseCases.findByFilter(filter));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById(UUID id){
        transactionUseCases.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
