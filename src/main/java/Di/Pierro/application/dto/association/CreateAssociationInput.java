package Di.Pierro.application.dto.association;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateAssociationInput(
    String associationType,
    String source,
    int confidenceLevel,
    boolean associationEnded,
    OffsetDateTime associationStart,
    OffsetDateTime associationEnd,
    UUID firstActorId,
    UUID secondActorId
) {
}
