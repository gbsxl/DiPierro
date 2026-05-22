package Di.Pierro.presentation.response.actor;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ActorResponse(
        UUID id,
        String address,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        boolean isActive
) {
}
