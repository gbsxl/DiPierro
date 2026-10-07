package Di.Pierro.application.dto.association;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateAssociationInput(
    @NotBlank
    @NotNull
    String associationType,

    String source,

    @Min(0)
    @Max(10)
    int confidenceLevel,

    boolean associationEnded,

    @NotNull
    @Past
    OffsetDateTime associationStart,

    OffsetDateTime associationEnd,

    @NotNull
    UUID firstActorId,

    @NotNull
    UUID secondActorId
) {
}
