package Di.Pierro.application.port.investigation.publicProcurement.model;

import java.util.UUID;

public record TransactionConnection(
        UUID transactionId,
        UUID fromEntityId,
        UUID toEntityId
){}