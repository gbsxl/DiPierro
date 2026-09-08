package Di.Pierro.application.port.investigation.publicProcurement.dataFetcher;


import Di.Pierro.application.dto.publicprocurement.PublicProcurementFilter;
import Di.Pierro.application.port.input.PublicProcurementUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.PublicProcurementInvestigationContext;
import Di.Pierro.domain.model.PublicProcurement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublicProcurementDataFetcher {
    private final PublicProcurementUseCases publicProcurementUseCases;

    public PublicProcurementDataFetcher(PublicProcurementUseCases publicProcurementUseCases) {
        this.publicProcurementUseCases = publicProcurementUseCases;
    }

    public void findPublicProcurement(String publicProcurementNumber, PublicProcurementInvestigationContext context){
        PublicProcurementFilter publicProcurementFilter = new PublicProcurementFilter(
                publicProcurementNumber,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<PublicProcurement> list = publicProcurementUseCases.findByFilter(publicProcurementFilter);
        if (list.isEmpty()) {
            throw new Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException("404", "Public procurement not found with number: " + publicProcurementNumber);
        }

        context.setProcurement(list.get(0));
    }
}
