package Di.Pierro.application.dto.actorindicator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record CreateActorIndicatorInput(
        @NotNull
        UUID actorId,

        @NotBlank
        @NotNull
        @Size(max = 150)
        String indicatorType,

        @NotBlank
        @NotNull
        @Size(max = 150)
        String value,

        String source,

        LocalDate date
) {
}
