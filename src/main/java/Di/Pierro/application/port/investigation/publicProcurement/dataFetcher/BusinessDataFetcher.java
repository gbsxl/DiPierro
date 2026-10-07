package Di.Pierro.application.port.investigation.publicProcurement.dataFetcher;

import Di.Pierro.application.dto.association.AssociationFilter;
import Di.Pierro.application.port.input.AssociationUseCases;
import Di.Pierro.application.port.input.BusinessUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.PublicProcurementInvestigationContext;
import Di.Pierro.domain.enums.AssociationType;
import Di.Pierro.domain.model.*;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class BusinessDataFetcher {
    private final AssociationUseCases associationUseCases;
    private final BusinessUseCases businessUseCases;

    public BusinessDataFetcher(AssociationUseCases associationUseCases, BusinessUseCases businessUseCases) {
        this.associationUseCases = associationUseCases;
        this.businessUseCases = businessUseCases;
    }

    public void getBusinessSeed(PublicProcurementInvestigationContext context){
        UUID publicProcurementActorId = context.getProcurement().getActor().getId();
        List<Business> businessSide = getBusinessSide(publicProcurementActorId);
        List<Business> governmentSide = getGovernmentSide(publicProcurementActorId);
        List<Business> allBusinessList = new ArrayList<>(businessSide);
        allBusinessList.addAll(governmentSide);

        context.setBusinessSide(businessSide);
        context.setGovernmentSide(governmentSide);
        context.setAllBusinessList(allBusinessList);
    }

    private List<Business> getBusinessSide(UUID publicProcurementActorId){
        List<AssociationType> associationTypeList = List.of(
                AssociationType.PARTICIPOU_LICITACAO,
                AssociationType.PARTICIPANTE_LICITACAO,
                AssociationType.VENCEDOR_LICITACAO
        );
        return getClassifyParticipatingBusiness(publicProcurementActorId, associationTypeList);
    }

    private List<Business> getGovernmentSide(UUID publicProcurementActorId){
        List<AssociationType> associationTypeList = List.of(
                AssociationType.CONTRATANTE_LICITACAO
        );
        return getClassifyParticipatingBusiness(publicProcurementActorId, associationTypeList);
    }


    private List<Business> getClassifyParticipatingBusiness(UUID publicProcurementActorId, List<AssociationType> associationTypeList){
        List<UUID> uuidList = getUuids(publicProcurementActorId, associationTypeList);
        return businessUseCases.findByActorIds(uuidList);
    }

    private List<UUID> getUuids(UUID publicProcurementActorId, List<AssociationType> associationTypeList) {
        AssociationFilter associationFilter = new AssociationFilter (
                null,
                null,
                null,
                null,
                publicProcurementActorId,
                null,
                null,
                associationTypeList,
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

        return associationList.stream()
                .map(association ->
                        association.getFirstActor().getId().equals(publicProcurementActorId)
                                ? association.getSecondActor().getId()
                                : association.getFirstActor().getId()
                )
                .toList();
    }

    public void fetchPublicProcurementWinner(PublicProcurementInvestigationContext context) {
        Business winnerBusiness = context.getAssociationList().stream()
                .filter(association ->
                        association.getAssociationType() == AssociationType.VENCEDOR_LICITACAO
                )
                .findFirst()
                .map(association ->
                        businessUseCases
                                .findByActorId(association.getFirstActor().getId())
                                .or(() -> businessUseCases.findByActorId(
                                        association.getSecondActor().getId()
                                ))
                                .orElseThrow(() ->
                                        new ResourceNotFoundException("404", "Business not found")
                                )
                )
                .orElse(null);

        if (winnerBusiness != null) {
            context.setPublicProcurementWinner(winnerBusiness);
        }
    }
}
