package Di.Pierro.infrastructure.persistence.transaction;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.TransactionRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Transaction;
import Di.Pierro.infrastructure.mapper.TransactionMapper;
import Di.Pierro.infrastructure.persistence.entity.TransactionEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaTransactionRepositoryAdapter implements TransactionRepository {

    JpaTransactionRepository jpaTransactionRepository;
    TransactionMapper transactionMapper;
    ActorRepository actorRepository;

    @Override
    public void save(Transaction transaction, UUID actorSenderId, UUID actorReceiver) {
        getActorById(actorSenderId).ifPresent(transaction::setActorSender);
        getActorById(actorReceiver).ifPresent(transaction::setActorReceiver);

        TransactionEntity transactionEntity = transactionMapper.toEntity(transaction);

        jpaTransactionRepository.save(transactionEntity);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return jpaTransactionRepository
                .findById(id)
                .map(transactionMapper::toDomain);
    }

    @Override
    public List<Transaction> findAll() {
        return jpaTransactionRepository
                .findAll()
                .stream()
                .map(transactionMapper::toDomain)
                .toList();
    }
    private Optional<Actor> getActorById(UUID id){
        return actorRepository.findById(id);
    }
}
