package Di.Pierro.application.dto.transaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateTransactionInput(
        BigDecimal value,
        String currency,
        OffsetDateTime transactionDate,
        UUID actorSenderId,
        UUID actorReceiverId
) {
}
