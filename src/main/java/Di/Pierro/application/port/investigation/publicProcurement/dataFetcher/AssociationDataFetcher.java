package Di.Pierro.application.port.investigation.publicProcurement.dataFetcher;

import Di.Pierro.application.dto.association.AssociationFilter;
import Di.Pierro.application.port.input.AssociationUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.PublicProcurementInvestigationContext;
import Di.Pierro.domain.model.Association;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class AssociationDataFetcher {
    private final AssociationUseCases associationUseCases;

    public AssociationDataFetcher(AssociationUseCases associationUseCases) {
        this.associationUseCases = associationUseCases;
    }

    public void fetchAllAssociations(PublicProcurementInvestigationContext context) {
        List<UUID> uuidList = new ArrayList<>(Stream.concat(
                context.getAllBusinessList().stream().map(business -> business.getActor().getId()),
                context.getAllPersonList().stream().map(person -> person.getActor().getId())
        ).toList());

        uuidList.add(context.getProcurement().getActor().getId());
        AssociationFilter associationFilter = new AssociationFilter(
                null,
                null,
                null,
                null,
                null,
                uuidList,
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

        List<Association> associationList = associationUseCases.findByFilter(associationFilter);

        context.setAssociationList(associationList);
    }

    public void getAllAssociation(PublicProcurementInvestigationContext context) {
        fetchAllAssociations(context);
    }
}
