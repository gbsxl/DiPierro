package Di.Pierro.application.port.usecase.transaction;

import Di.Pierro.application.dto.transaction.CreateTransactionInput;
import Di.Pierro.application.dto.transaction.TransactionFilter;
import Di.Pierro.application.port.input.TransactionUseCases;
import Di.Pierro.application.port.output.TransactionRepository;
import Di.Pierro.domain.enums.Currency;
import Di.Pierro.domain.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionUseCasesImplementation implements TransactionUseCases {
    private final TransactionRepository transactionRepository;

    public TransactionUseCasesImplementation(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createTransaction(CreateTransactionInput createTransactionInput) {
        Transaction transaction = Transaction.createTransaction(
                createTransactionInput.value(),
                Currency.fromCode(createTransactionInput.currency()),
                createTransactionInput.transactionDate()
        );

        transactionRepository.save(transaction, createTransactionInput.actorSenderId(), createTransactionInput.actorReceiverId());
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> findByFilter(TransactionFilter filter) {
        return transactionRepository.findByFilter(filter);
    }

    @Override
    public void deleteById(UUID id) {
        transactionRepository.deleteById(id);
    }

}
