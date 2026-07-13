package Di.Pierro.application.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateTransactionInput(
        @NotNull
        @NotBlank
        BigDecimal value,

        @NotBlank
        @NotNull
        @Size(min = 3, max = 3)
        String currency,

        @NotNull
        @Past
        OffsetDateTime transactionDate,

        @NotNull
        UUID actorSenderId,

        @NotNull
        UUID actorReceiverId
) {
}
