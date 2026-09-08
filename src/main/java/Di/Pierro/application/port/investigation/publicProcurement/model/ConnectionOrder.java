package Di.Pierro.application.port.investigation.publicProcurement.model;

public record ConnectionOrder(
        Long currentItemId,
        Long previousItemId
) {
}
