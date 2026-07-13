package Di.Pierro.application.dto.asset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAssetInput(
        @NotNull
        UUID personId,

        @NotBlank
        @NotNull
        @Size(max = 30)
        String type,

        String description,

        BigDecimal estimatedValue,

        String source,

        boolean stillHaveIt,

        LocalDate acquiredAt,

        LocalDate mappedAt
) {
}
