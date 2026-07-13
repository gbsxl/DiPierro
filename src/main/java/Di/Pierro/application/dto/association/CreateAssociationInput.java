package Di.Pierro.application.dto.association;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateAssociationInput(
    @NotBlank
    @NotNull
    String associationType,

    String source,

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
