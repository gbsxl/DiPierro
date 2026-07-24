package Di.Pierro.application.dto.transaction;

import Di.Pierro.domain.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record TransactionFilter(

        UUID senderId,

        UUID receiverId,

        @Size(max = 100, message = "senderIds cannot exceed 100 items")
        List<UUID> senderIds,

        @Size(max = 100, message = "receiverIds cannot exceed 100 items")
        List<UUID> receiverIds,

        @Size(max = 100, message = "participantIds cannot exceed 100 items")
        List<UUID> participantIds,

        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "minimumValue cannot be negative"
        )
        BigDecimal minimumValue,

        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "maximumValue cannot be negative"
        )
        BigDecimal maximumValue,

        OffsetDateTime minimumDate,

        OffsetDateTime maximumDate,

        Currency currency
) {
}