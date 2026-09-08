package Di.Pierro.application.dto.redflag;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record RedFlagFilter(

        UUID actorId,

        List<UUID> actorIds,

        UUID publicProcurementId,

        UUID transactionId,

        UUID associationId,

        @Size(max = 255)
        String type,

        @Min(1)
        @Max(10)
        Integer severity
) {
}
