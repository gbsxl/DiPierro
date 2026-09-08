package Di.Pierro.application.port.investigation.publicProcurement.model;

public record TransactionItemBySide(
        TransactionConnection transactionConnection,
        Side side
){}