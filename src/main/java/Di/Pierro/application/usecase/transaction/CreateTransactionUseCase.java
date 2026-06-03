package Di.Pierro.application.usecase.transaction;

import Di.Pierro.application.dto.transaction.CreateTransactionInput;
import Di.Pierro.application.port.output.TransactionRepository;
import Di.Pierro.domain.enums.Currency;
import Di.Pierro.domain.model.Transaction;

public class CreateTransactionUseCase {
    TransactionRepository transactionRepository;

    public CreateTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void execute(CreateTransactionInput createTransactionInput){
        Transaction transaction = Transaction.createTransaction(
                createTransactionInput.value(),
                Currency.fromCode(createTransactionInput.currency()),
                createTransactionInput.transactionDate()
        );

        transactionRepository.save(transaction, createTransactionInput.actorSenderId(), createTransactionInput.actorReceiverId());
    }
}
