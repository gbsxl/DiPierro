package Di.Pierro.application.usecase.transaction;

import Di.Pierro.application.port.output.TransactionRepository;
import Di.Pierro.domain.model.Transaction;

import java.util.List;

public class SearchTransactionUseCase {
    TransactionRepository transactionRepository;

    public SearchTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> execute(){
        return transactionRepository.findAll();
    }
}
