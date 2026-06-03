package Di.Pierro.application.usecase.transaction;

import Di.Pierro.application.port.output.TransactionRepository;
import Di.Pierro.domain.model.Transaction;

import java.util.Optional;
import java.util.UUID;

public class GetTransactionByIdUseCase {
    TransactionRepository transactionRepository;

    public GetTransactionByIdUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Optional<Transaction> execute(UUID id){
        return transactionRepository.findById(id);
    }
}
