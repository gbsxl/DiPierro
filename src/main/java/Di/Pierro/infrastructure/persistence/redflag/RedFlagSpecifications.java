package Di.Pierro.infrastructure.persistence.redflag;

import Di.Pierro.infrastructure.persistence.entity.ActorEntity;
import Di.Pierro.infrastructure.persistence.entity.RedFlagEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class RedFlagSpecifications {

    public static Specification<RedFlagEntity> hasActor(UUID actorId) {
        return (root, query, builder) -> {
            query.distinct(true);
            Join<RedFlagEntity, ActorEntity> actorsJoin = root.join("actors");
            return builder.equal(actorsJoin.get("id"), actorId);
        };
    }

    public static Specification<RedFlagEntity> hasActorIn(List<UUID> actorIds) {
        return (root, query, builder) -> {
            if (actorIds == null || actorIds.isEmpty()) {
                return null;
            }
            query.distinct(true);
            Join<RedFlagEntity, ActorEntity> actorsJoin = root.join("actors");
            return actorsJoin.get("id").in(actorIds);
        };
    }

    public static Specification<RedFlagEntity> hasPublicProcurement(UUID publicProcurementId) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("publicProcurement").get("id"),
                        publicProcurementId
                );
    }

    public static Specification<RedFlagEntity> hasTransaction(UUID transactionId) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("transactionId"),
                        transactionId
                );
    }

    public static Specification<RedFlagEntity> hasAssociation(UUID associationId) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("associationId"),
                        associationId
                );
    }

    public static Specification<RedFlagEntity> hasType(String type) {
        return (root, query, builder) ->
                builder.like(
                        builder.lower(root.get("type")),
                        "%" + type.toLowerCase() + "%"
                );
    }

    public static Specification<RedFlagEntity> hasSeverity(Integer severity) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("severity"),
                        severity
                );
    }
}
