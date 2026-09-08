package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.transaction.CreateTransactionInput;
import Di.Pierro.application.dto.transaction.TransactionFilter;
import Di.Pierro.domain.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionUseCases {
    void createTransaction(CreateTransactionInput createTransactionInput);
    List<Transaction> findAll();
    List<Transaction> findAllByIds(List<UUID> ids);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findByFilter(TransactionFilter filter);
    void deleteById(UUID id);
}
