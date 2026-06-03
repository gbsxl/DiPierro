package Di.Pierro.infrastructure.mapper;

import Di.Pierro.domain.model.Transaction;
import Di.Pierro.infrastructure.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ActorMapper.class)
public interface TransactionMapper {
    Transaction toDomain(TransactionEntity transactionEntity);
    TransactionEntity toEntity(Transaction transaction);
}
