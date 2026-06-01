package Di.Pierro.application.dto.association;

import Di.Pierro.domain.enums.AssociationType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateAssociationInput(
    AssociationType associationType,
    String source,
    int confidenceLevel,
    boolean associationEnded,
    OffsetDateTime associationStart,
    OffsetDateTime associationEnd,
    UUID firstActorId,
    UUID secondActorId
) {
}
