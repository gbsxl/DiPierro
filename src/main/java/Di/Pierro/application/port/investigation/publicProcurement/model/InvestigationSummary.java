package Di.Pierro.application.port.investigation.publicProcurement.model;

public record InvestigationSummary(
        int totalRedFlags,
        int criticalRedFlags,
        int highRedFlags,
        int mediumRedFlags,
        int lowRedFlags
) {
}