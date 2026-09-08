package Di.Pierro.application.port.investigation.publicProcurement.model;

import Di.Pierro.domain.model.RedFlag;

import java.util.List;
import java.util.UUID;

public record InvestigationResult(
        UUID publicProcurementId,
        InvestigationStatus status,
        InvestigationSummary summary,
        List<RedFlag> redFlags
) {
}


