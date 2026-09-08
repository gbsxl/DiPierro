package Di.Pierro.application.dto.association;

import Di.Pierro.domain.enums.AssociationType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record AssociationFilter(

        UUID firstActorId,

        UUID secondActorId,

        @Size(max = 100, message = "firstActorIds cannot exceed 100 items")
        List<UUID> firstActorIds,

        @Size(max = 100, message = "secondActorIds cannot exceed 100 items")
        List<UUID> secondActorIds,

        UUID actorId,

        @Size(max = 100, message = "actorIds cannot exceed 100 items")
        List<UUID> actorIds,

        AssociationType associationType,

        @Size(max = 50, message = "associationTypes cannot exceed 50 items")
        List<AssociationType> associationTypes,

        @Size(max = 255, message = "source cannot exceed 255 characters")
        String source,

        @Size(max = 50, message = "sources cannot exceed 50 items")
        List<String> sources,

        @Min(value = 0, message = "minimumConfidenceLevel cannot be negative")
        @Max(value = 100, message = "minimumConfidenceLevel cannot exceed 100")
        Integer minimumConfidenceLevel,

        @Min(value = 0, message = "maximumConfidenceLevel cannot be negative")
        @Max(value = 100, message = "maximumConfidenceLevel cannot exceed 100")
        Integer maximumConfidenceLevel,

        Boolean associationEnded,

        OffsetDateTime startAfterDate,

        OffsetDateTime startBeforeDate,

        OffsetDateTime endAfterDate,

        OffsetDateTime endBeforeDate
) {
}
