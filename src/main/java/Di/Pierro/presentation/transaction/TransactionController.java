package Di.Pierro.presentation.transaction;

import Di.Pierro.application.dto.transaction.CreateTransactionInput;
import Di.Pierro.application.usecase.transaction.CreateTransactionUseCase;
import Di.Pierro.application.usecase.transaction.GetTransactionByIdUseCase;
import Di.Pierro.application.usecase.transaction.SearchTransactionUseCase;
import Di.Pierro.domain.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    CreateTransactionUseCase createTransactionUseCase;
    GetTransactionByIdUseCase getTransactionByIdUseCase;
    SearchTransactionUseCase searchTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase, GetTransactionByIdUseCase getTransactionByIdUseCase, SearchTransactionUseCase searchTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getTransactionByIdUseCase = getTransactionByIdUseCase;
        this.searchTransactionUseCase = searchTransactionUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreateTransactionInput createTransactionInput){
        createTransactionUseCase.execute(createTransactionInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> findById(@PathVariable UUID id){
        Optional<Transaction> optionalTransaction = getTransactionByIdUseCase.execute(id);
        return optionalTransaction.map(
                Transaction-> ResponseEntity.ok(optionalTransaction.get())
        ).orElseGet( () -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> findAll(){
        return ResponseEntity.ok(searchTransactionUseCase.execute());
    }

}
