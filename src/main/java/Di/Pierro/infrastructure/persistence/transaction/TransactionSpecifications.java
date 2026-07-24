package Di.Pierro.infrastructure.persistence.transaction;

import Di.Pierro.domain.enums.Currency;
import Di.Pierro.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class TransactionSpecifications {

    public static Specification<TransactionEntity> hasSender(UUID senderId) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("actorSender").get("id"),
                        senderId
                );

    }

    public static Specification<TransactionEntity> hasReceiver(UUID receiverId){
        return (root, query, builder) ->
                builder.equal(
                        root.get("actorReceiver").get("id"),
                        receiverId
                );
    }

    public static Specification<TransactionEntity> hasCurrency(Currency currency){
        return (root, query, builder) ->
                 builder.equal(
                         root.get("currency"),
                         currency
                 );
    }

    public static Specification<TransactionEntity> minimumValue(BigDecimal value){
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(
                        root.get("value"),
                        value
                );
    }

    public static Specification<TransactionEntity> maximumValue(BigDecimal value) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(
                        root.get("value"),
                        value
                );
    }

    public static Specification<TransactionEntity> beforeDate(OffsetDateTime date) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(
                        root.get("transactionDate"),
                        date
                );
    }

    public static Specification<TransactionEntity> afterDate(OffsetDateTime date) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(
                        root.get("transactionDate"),
                        date
                );

    }

    public static Specification<TransactionEntity> hasSenderIds(List<UUID> senderIdList){
        return (root, query, builder) ->
                root.get("actorSender")
                        .get("id")
                        .in(senderIdList);
    }

    public static Specification<TransactionEntity> hasReceiverIds(List<UUID> receiverIds) {
        return (root, query, builder) ->
                root.get("actorReceiver")
                        .get("id")
                        .in(receiverIds);
    }

    public static Specification<TransactionEntity> hasParticipantIds(List<UUID> participantIds) {
        return (root, query, builder) ->

                builder.or(

                        root.get("actorSender")
                                .get("id")
                                .in(participantIds),

                        root.get("actorReceiver")
                                .get("id")
                                .in(participantIds)

                );
    }

}
