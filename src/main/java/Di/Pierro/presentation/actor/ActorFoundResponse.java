package Di.Pierro.presentation.actor;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ActorFoundResponse(
        UUID id,
        String address,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        boolean isActive
) {
}
