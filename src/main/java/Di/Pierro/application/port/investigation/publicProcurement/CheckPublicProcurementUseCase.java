package Di.Pierro.application.port.investigation.publicProcurement;

import Di.Pierro.application.port.investigation.publicProcurement.model.InvestigationResult;

public interface CheckPublicProcurementUseCase {
    InvestigationResult investigatePublicProcurement(String publicProcurementNumber);
}