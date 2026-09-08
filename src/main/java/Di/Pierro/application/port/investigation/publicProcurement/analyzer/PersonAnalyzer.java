package Di.Pierro.application.port.investigation.publicProcurement.analyzer;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.port.input.BusinessUseCases;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.*;
import Di.Pierro.domain.model.Business;
import Di.Pierro.domain.model.Person;
import Di.Pierro.domain.model.RedFlag;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PersonAnalyzer {
    private final BusinessUseCases businessUseCases;
    private final RedFlagUseCases redFlagUseCases;

    public PersonAnalyzer(BusinessUseCases businessUseCases, RedFlagUseCases redFlagUseCases) {
        this.businessUseCases = businessUseCases;
        this.redFlagUseCases = redFlagUseCases;
    }

    public void identifySharedQsaBetweenGovernmentAndBusiness(PublicProcurementInvestigationContext context){
        List<QSA> qsaList = context.getQsaList();
        if (qsaList == null || qsaList.isEmpty()) {
            return;
        }
        Map<UUID, Person> allBusinessSidePersonList = context.getAllBusinessSidePersonList();
        Map<UUID, Person> allGovernmentSidePersonList = context.getAllGovernmentSidePersonList();
        List<SharedQsa> uuidListIntersectionGovernmentBusiness = new ArrayList<>();

        for (QSA qsa : qsaList) {
            List<UUID> uuidList = qsa.personList().stream().map(person -> person.getActor().getId()).toList();
            List<QsaItem> uuidListBusinessPersonInQsa = getQsaItems(allBusinessSidePersonList, uuidList, Side.BUSINESS);
            List<QsaItem> uuidListGovernmentPersonInQsa = getQsaItems(allGovernmentSidePersonList, uuidList, Side.GOVERNMENT);
            boolean isSharedQsa = hasSharedQsa(uuidListBusinessPersonInQsa, uuidListGovernmentPersonInQsa);
            if (isSharedQsa) uuidListIntersectionGovernmentBusiness.add(
                    generateSharedQsa(qsa.businessActorId(), uuidListBusinessPersonInQsa,uuidListGovernmentPersonInQsa)
            );
        }

        if(!uuidListIntersectionGovernmentBusiness.isEmpty()) context.getRedFlags().addAll(
                generateSharedQsaRedFlag(context, uuidListIntersectionGovernmentBusiness)
        );
    }

    public void identifyLinkBetweenPeopleOnTheGovernmentSideAndPublicProcurementParticipants(PublicProcurementInvestigationContext context) {
        PersonGraph governmentPersonGraph = context.getGovernmentPersonGraph();
        PersonGraph businessPersonGraph = context.getBusinessPersonGraph();
        PersonGraphIntersection personGraphIntersection = context.getPersonGraphIntersection();

        if (governmentPersonGraph == null || businessPersonGraph == null || personGraphIntersection == null) {
            return;
        }

        Set<UUID> intersectionActorIds = new HashSet<>();

        for (PersonGraphItem govItem : governmentPersonGraph.getPersonGraphItemList()) {
            if (!businessPersonGraph.hasUUIDInThePersonGraph(govItem.actorId()).isEmpty()) {
                intersectionActorIds.add(govItem.actorId());
            }
        }

        if (intersectionActorIds.isEmpty()) {
            return;
        }

        Set<List<UUID>> processedTrajectoryActorIdSets = new HashSet<>();
        List<RedFlag> newRedFlags = new ArrayList<>();

        UUID procurementId = context.getProcurement() != null ? context.getProcurement().getId() : null;

        for (UUID intersectionActorId : intersectionActorIds) {
            List<PersonGraphItem> govItems = governmentPersonGraph.hasUUIDInThePersonGraph(intersectionActorId);
            List<PersonGraphItem> busItems = businessPersonGraph.hasUUIDInThePersonGraph(intersectionActorId);

            for (PersonGraphItem govItem : govItems) {
                for (PersonGraphItem busItem : busItems) {
                    LinkedList<ActorConnectionNode> fullTrajectory = combinePaths(
                            govItem.actorIdConnectionsOrder(),
                            busItem.actorIdConnectionsOrder()
                    );

                    personGraphIntersection.addTrajectory(intersectionActorId, fullTrajectory);

                    List<UUID> actorIdsSequence = fullTrajectory.stream()
                            .map(ActorConnectionNode::actorId)
                            .toList();

                    if (processedTrajectoryActorIdSets.add(actorIdsSequence)) {
                        String description = generateTrajectoryDescription(fullTrajectory);
                        CreateRedFlagInput input = new CreateRedFlagInput(
                                actorIdsSequence,
                                procurementId,
                                null,
                                null,
                                "Possible link between individuals on the government side and the participatory side of the public procurement",
                                9,
                                description
                        );
                        RedFlag redFlag = redFlagUseCases.createRedFlag(input);
                        newRedFlags.add(redFlag);
                    }
                }
            }
        }

        context.getRedFlags().addAll(newRedFlags);
    }

    public void identifyProbableFraudulentCpfUsage(PublicProcurementInvestigationContext context){
        if (context.getProcurement() == null || context.getProcurement().getOpeningDate() == null) {
            return;
        }

        List<Person> allPersonList = context.getAllPersonList();
        List<RedFlag> newRedFlags = new ArrayList<>();
        LocalDate publicProcurementDate = context.getProcurement().getOpeningDate();
        UUID procurementActorId = context.getProcurement().getActor() != null ? context.getProcurement().getActor().getId() : null;

        for(Person person : allPersonList){
            if(Boolean.FALSE.equals(person.getIsAlive())){
                LocalDate deathDate = person.getDeathDate();
                if (deathDate != null) {
                    boolean isBeforePublicProcurementOpening = deathDate.isBefore(publicProcurementDate);
                    boolean deathOccurredInSuspiciousDate = deathDate.isAfter(publicProcurementDate) && deathDate.isBefore(publicProcurementDate.plusMonths(3));

                    if(isBeforePublicProcurementOpening || deathOccurredInSuspiciousDate){
                        CreateRedFlagInput input = createCpfFraudRedFlagInputBySeverity(isBeforePublicProcurementOpening, person, procurementActorId);
                        newRedFlags.add(redFlagUseCases.createRedFlag(input));
                    }
                }
            }
        }
        context.getRedFlags().addAll(newRedFlags);
    }

    public void identifyProbablyUseOfCpfToFraudulentPurposes(PublicProcurementInvestigationContext context) {
        identifyProbableFraudulentCpfUsage(context);
    }

    private CreateRedFlagInput createCpfFraudRedFlagInputBySeverity(boolean isHighSeverity, Person person, UUID publicProcurementActorId){
        String typeIfIsSeverity = "Deceased participant in the public procurement";
        String descriptionIfIsSeverity = "A participant in the public procurement process may have died before it took place.";

        String typeIfIsNotSeverity = "Suspicious date of death of a public procurement participant";
        String descriptionIfIsNotSeverity = "The date of death raises concerns about potential fraudulent use of the CPF, given that the individual died less than three months after the public procurement.";

        return new CreateRedFlagInput(
                List.of(person.getActor().getId()),
                publicProcurementActorId,
                null,
                null,
                isHighSeverity ? typeIfIsSeverity : typeIfIsNotSeverity,
                isHighSeverity ? 10 : 7,
                isHighSeverity ? descriptionIfIsSeverity : descriptionIfIsNotSeverity
        );
    }

    private LinkedList<ActorConnectionNode> combinePaths(LinkedList<ActorConnectionNode> govOrder, LinkedList<ActorConnectionNode> busOrder) {
        LinkedList<ActorConnectionNode> fullTrajectory = new LinkedList<>();
        if (govOrder != null) {
            fullTrajectory.addAll(govOrder);
        }

        if (busOrder != null && busOrder.size() > 1) {
            for (int i = busOrder.size() - 2; i >= 0; i--) {
                fullTrajectory.add(busOrder.get(i));
            }
        }
        return fullTrajectory;
    }

    private String generateTrajectoryDescription(LinkedList<ActorConnectionNode> trajectory) {
        StringBuilder sb = new StringBuilder("Found connection trajectory crossing government and business sides: ");
        for (int i = 0; i < trajectory.size(); i++) {
            ActorConnectionNode node = trajectory.get(i);
            sb.append(node.actorType()).append("(").append(node.actorId()).append(")");
            if (i < trajectory.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    private List<RedFlag> generateSharedQsaRedFlag(PublicProcurementInvestigationContext context, List<SharedQsa> sharedQsas){
        List<RedFlag> redFlags = new ArrayList<>();
        Map<UUID, Business> businessMap = context.getAllBusinessList().stream()
                .collect(Collectors.toMap(b -> b.getActor().getId(), b -> b, (a, b) -> a));

        for (SharedQsa sharedQsa : sharedQsas){
            Business business = businessMap.get(sharedQsa.businessActorId());
            if(business != null){
                String businessName = business.getFantasyName() != null ? business.getFantasyName() : business.getLegalName();
                List<UUID> actorIds = new ArrayList<>();
                actorIds.add(sharedQsa.businessActorId());
                sharedQsa.sharedQsaItemList().forEach(qsaItem -> actorIds.add(qsaItem.personActorId()));
                CreateRedFlagInput input = new CreateRedFlagInput(
                        actorIds,
                        context.getProcurement() != null ? context.getProcurement().getId() : null,
                        null,
                        null,
                        "Shared QSA between Government and Business",
                        8,
                        "Found shared partners/representatives between business " + businessName + " and government side."
                );
                RedFlag redFlag = redFlagUseCases.createRedFlag(input);
                redFlags.add(redFlag);
            }
        }
        return redFlags;
    }

    private SharedQsa generateSharedQsa(UUID businessActorId, List<QsaItem> qsaItemBusinessList, List<QsaItem> qsaItemGovernmentList){
        List<UUID> businessPersonActorIds = qsaItemBusinessList.stream().map(qsaItem -> qsaItem.personActorId).toList();
        List<QsaItem> onlySharedQsa = qsaItemGovernmentList.stream().filter(qsaItem -> businessPersonActorIds.contains(qsaItem.personActorId)).toList();
        return new SharedQsa(businessActorId, onlySharedQsa);
    }

    private List<QsaItem> getQsaItems(Map<UUID, Person> uuidPersonMap, List<UUID> uuidListToCheck, Side side){
        List<QsaItem> qsaItems = new ArrayList<>();
        for (UUID uuid : uuidListToCheck){
            if (uuidPersonMap.containsKey(uuid)){
                qsaItems.add(new QsaItem(uuid, side));
            }
        }
        return qsaItems;
    }

    private boolean hasSharedQsa(List<QsaItem> qsaItemListBusiness, List<QsaItem> qsaItemListGovernment){
        List<UUID> uuidBusinessList = qsaItemListBusiness.stream().map(qsaItem -> qsaItem.personActorId).toList();
        List<UUID> uuidGovernmentList = qsaItemListGovernment.stream().map(qsaItem -> qsaItem.personActorId).toList();

        return uuidBusinessList.stream().anyMatch(uuidGovernmentList::contains);
    }

    record SharedQsa(UUID businessActorId, List<QsaItem> sharedQsaItemList){}
    record QsaItem(UUID personActorId, Side side){}
}
