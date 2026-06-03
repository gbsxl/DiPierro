package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.output.TransactionRepository;
import Di.Pierro.application.usecase.transaction.CreateTransactionUseCase;
import Di.Pierro.application.usecase.transaction.GetTransactionByIdUseCase;
import Di.Pierro.application.usecase.transaction.SearchTransactionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TransactionBeanConfig {
    @Bean
    public CreateTransactionUseCase createTransactionUseCase(TransactionRepository transactionRepository){
        return new CreateTransactionUseCase(transactionRepository);
    }


    @Bean
    public GetTransactionByIdUseCase getTransactionByIdUseCase(TransactionRepository transactionRepository){
        return new GetTransactionByIdUseCase(transactionRepository);
    }

    @Bean
    public SearchTransactionUseCase searchTransactionUseCase(TransactionRepository transactionRepository){
        return new SearchTransactionUseCase(transactionRepository);
    }
}
