package Di.Pierro.application.port.output;

import Di.Pierro.application.dto.transaction.TransactionFilter;
import Di.Pierro.domain.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    void save(Transaction transaction, UUID actorSenderId, UUID actorReceiver);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findAll();
    List<Transaction> findAllByIds(List<UUID> ids);
    List<Transaction> findByFilter(TransactionFilter filter);
    void deleteById(UUID id);
}
