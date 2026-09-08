package Di.Pierro.infrastructure.persistence.association;

import Di.Pierro.domain.enums.AssociationType;
import Di.Pierro.infrastructure.persistence.entity.AssociationEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class AssociationSpecifications {

    public static Specification<AssociationEntity> hasFirstActorId(UUID firstActorId) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("firstActor").get("id"),
                        firstActorId
                );
    }

    public static Specification<AssociationEntity> hasSecondActorId(UUID secondActorId) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("secondActor").get("id"),
                        secondActorId
                );
    }

    public static Specification<AssociationEntity> hasFirstActorIds(List<UUID> firstActorIds) {
        return (root, query, builder) ->
                root.get("firstActor")
                        .get("id")
                        .in(firstActorIds);
    }

    public static Specification<AssociationEntity> hasSecondActorIds(List<UUID> secondActorIds) {
        return (root, query, builder) ->
                root.get("secondActor")
                        .get("id")
                        .in(secondActorIds);
    }

    public static Specification<AssociationEntity> hasActorId(UUID actorId) {
        return (root, query, builder) ->
                builder.or(
                        builder.equal(root.get("firstActor").get("id"), actorId),
                        builder.equal(root.get("secondActor").get("id"), actorId)
                );
    }

    public static Specification<AssociationEntity> hasActorIds(List<UUID> actorIds) {
        return (root, query, builder) ->
                builder.or(
                        root.get("firstActor").get("id").in(actorIds),
                        root.get("secondActor").get("id").in(actorIds)
                );
    }

    public static Specification<AssociationEntity> hasAssociationType(AssociationType associationType) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("associationType"),
                        associationType
                );
    }

    public static Specification<AssociationEntity> hasAssociationTypes(List<AssociationType> associationTypes) {
        return (root, query, builder) ->
                root.get("associationType")
                        .in(associationTypes);
    }

    public static Specification<AssociationEntity> containsSource(String source) {
        return (root, query, builder) ->
                builder.like(
                        builder.lower(root.get("source")),
                        "%" + source.toLowerCase() + "%"
                );
    }

    public static Specification<AssociationEntity> hasSources(List<String> sources) {
        return (root, query, builder) ->
                root.get("source")
                        .in(sources);
    }

    public static Specification<AssociationEntity> minimumConfidenceLevel(Integer value) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(
                        root.get("confidenceLevel"),
                        value
                );
    }

    public static Specification<AssociationEntity> maximumConfidenceLevel(Integer value) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(
                        root.get("confidenceLevel"),
                        value
                );
    }

    public static Specification<AssociationEntity> isAssociationEnded(Boolean ended) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("associationEnded"),
                        ended
                );
    }

    public static Specification<AssociationEntity> startAfterDate(OffsetDateTime date) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(
                        root.get("associationStart"),
                        date
                );
    }

    public static Specification<AssociationEntity> startBeforeDate(OffsetDateTime date) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(
                        root.get("associationStart"),
                        date
                );
    }

    public static Specification<AssociationEntity> endAfterDate(OffsetDateTime date) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(
                        root.get("associationEnd"),
                        date
                );
    }

    public static Specification<AssociationEntity> endBeforeDate(OffsetDateTime date) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(
                        root.get("associationEnd"),
                        date
                );
    }
}
