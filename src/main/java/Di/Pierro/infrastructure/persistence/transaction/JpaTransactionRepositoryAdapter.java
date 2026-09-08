package Di.Pierro.infrastructure.persistence.transaction;

import Di.Pierro.application.dto.transaction.TransactionFilter;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.TransactionRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Transaction;
import Di.Pierro.infrastructure.mapper.TransactionMapper;
import Di.Pierro.infrastructure.persistence.entity.TransactionEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaTransactionRepositoryAdapter implements TransactionRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaTransactionRepositoryAdapter.class);

    private final JpaTransactionRepository jpaTransactionRepository;
    private final TransactionMapper transactionMapper;
    private final ActorRepository actorRepository;

    @Override
    public void save(Transaction transaction, UUID actorSenderId, UUID actorReceiverId) {
        getActorById(actorSenderId).ifPresent(transaction::setActorSender);
        getActorById(actorReceiverId).ifPresent(transaction::setActorReceiver);
        log.debug("Saving transaction senderId={} receiverId={}", actorSenderId, actorReceiverId);
        jpaTransactionRepository.save(transactionMapper.toEntity(transaction));
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        log.debug("Looking up transaction by id={}", id);
        return jpaTransactionRepository.findById(id).map(transactionMapper::toDomain);
    }

    @Override
    public List<Transaction> findAll() {
        log.debug("Fetching all transactions");
        return jpaTransactionRepository.findAll().stream().map(transactionMapper::toDomain).toList();
    }

    @Override
    public List<Transaction> findAllByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        log.debug("Fetching transactions by ids list count={}", ids.size());
        return jpaTransactionRepository.findAllById(ids).stream().map(transactionMapper::toDomain).toList();
    }

    @Override
    public List<Transaction> findByFilter(TransactionFilter filter) {
        log.debug("Searching transactions by filter={}", filter);
        Specification<TransactionEntity> spec = Specification.unrestricted();

        if (filter.senderId() != null) {
            spec = spec.and(TransactionSpecifications.hasSender(filter.senderId()));
        }
        if (filter.receiverId() != null) {
            spec = spec.and(TransactionSpecifications.hasReceiver(filter.receiverId()));
        }
        if (filter.currency() != null) {
            spec = spec.and(TransactionSpecifications.hasCurrency(filter.currency()));
        }
        if (filter.minimumValue() != null) {
            spec = spec.and(TransactionSpecifications.minimumValue(filter.minimumValue()));
        }
        if (filter.maximumValue() != null) {
            spec = spec.and(TransactionSpecifications.maximumValue(filter.maximumValue()));
        }
        if (filter.minimumDate() != null) {
            spec = spec.and(TransactionSpecifications.beforeDate(filter.minimumDate()));
        }
        if (filter.maximumDate() != null) {
            spec = spec.and(TransactionSpecifications.afterDate(filter.maximumDate()));
        }
        if (filter.senderIds() != null) {
            spec = spec.and(TransactionSpecifications.hasSenderIds(filter.senderIds()));
        }
        if (filter.receiverIds() != null) {
            spec = spec.and(TransactionSpecifications.hasReceiverIds(filter.receiverIds()));
        }
        if (filter.participantIds() != null) {
            spec = spec.and(TransactionSpecifications.hasParticipantIds(filter.participantIds()));
        }

        return jpaTransactionRepository.findAll(spec).stream().map(transactionMapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting transaction id={}", id);
        jpaTransactionRepository.deleteById(id);
    }

    private Optional<Actor> getActorById(UUID id) {
        return actorRepository.findById(id);
    }
}
