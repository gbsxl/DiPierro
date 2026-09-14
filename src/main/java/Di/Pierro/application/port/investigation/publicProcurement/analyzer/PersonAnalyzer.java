package Di.Pierro.application.port.investigation.publicProcurement.analyzer;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.port.input.PersonUseCases;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.*;
import Di.Pierro.domain.model.*;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PersonAnalyzer {
    private final RedFlagUseCases redFlagUseCases;
    private final PersonUseCases personUseCases;

    public PersonAnalyzer(RedFlagUseCases redFlagUseCases, PersonUseCases personUseCases) {
        this.redFlagUseCases = redFlagUseCases;
        this.personUseCases = personUseCases;
    }

    public void identifyFrontMans(PublicProcurementInvestigationContext context){
        checkFrontMans(context);
    }

    private void checkFrontMans(PublicProcurementInvestigationContext context){
        Map<UUID, PersonCheckFrontManData> personCheckFrontManDataMap = getPersonCheckFrontManMap(context);

        personCheckFrontManDataMap.forEach((uuid, personCheckFrontManData) -> {
            boolean isTooYoung = personCheckFrontManData.age != null && personCheckFrontManData.age <= 24;
            boolean isTooOld = personCheckFrontManData.age != null && personCheckFrontManData.age >= 60;
            boolean hasEnforcementProceedings = hasThisAtActorIndicators(List.of("execucao", "execução"), personCheckFrontManData.indicators);
            boolean hasBankruptcies = hasThisAtActorIndicators(List.of("falencia", "falência"), personCheckFrontManData.indicators);
            boolean hasDebtProtests = hasThisAtActorIndicators(List.of("protesto", "prostesto"), personCheckFrontManData.indicators);
            boolean hasAtLeastAEnforcementProceedingsOrBankruptciesOrDebtProtests = hasEnforcementProceedings || hasBankruptcies || hasDebtProtests;

            Map<UUID, Boolean> patrimonyIsTooSmallForCapitalStockByBusiness = new HashMap<>();

            if (personCheckFrontManData.stockCapitalByBusinessForAssociate != null) {
                personCheckFrontManData.stockCapitalByBusinessForAssociate.forEach((businessActorUUID, stockCapitalByBusinessPerAssociate) -> {
                    BigDecimal totalAssets = personCheckFrontManData.totalEstimatedAssetsValue != null ? personCheckFrontManData.totalEstimatedAssetsValue : BigDecimal.ZERO;
                    boolean isLessThanStockPerAssociate = stockCapitalByBusinessPerAssociate != null && totalAssets.compareTo(stockCapitalByBusinessPerAssociate) < 0;
                    if (isLessThanStockPerAssociate) {
                        patrimonyIsTooSmallForCapitalStockByBusiness.put(businessActorUUID, true);
                    }
                });
            }

            boolean hasTooSmallItems = patrimonyIsTooSmallForCapitalStockByBusiness.containsValue(true);
            boolean hasRedFlags = isTooYoung || isTooOld || hasAtLeastAEnforcementProceedingsOrBankruptciesOrDebtProtests || hasTooSmallItems;

            if(hasRedFlags) {
                context.getRedFlags().add(createFrontMansRedFlag(
                        isTooYoung,
                        isTooOld,
                        hasEnforcementProceedings,
                        hasBankruptcies,
                        hasDebtProtests,
                        patrimonyIsTooSmallForCapitalStockByBusiness,
                        uuid,
                        context.getProcurement() != null ? context.getProcurement().getId() : null
                ));
            }
        });
    }

    private RedFlag createFrontMansRedFlag(boolean isTooYoung, boolean isTooOld, boolean hasEnforcementProceedings, boolean hasBankruptcies, boolean hasDebtProtests, Map<UUID, Boolean> tooSmallForCapitalStock, UUID personActorId, UUID publicProcurementId){
        boolean hasAgeFlag = isTooYoung || isTooOld;

        int procCount = 0;
        if (hasEnforcementProceedings) procCount++;
        if (hasBankruptcies) procCount++;
        if (hasDebtProtests) procCount++;
        boolean hasProcFlag = procCount > 0;

        boolean hasCapitalStockFlag = tooSmallForCapitalStock != null && tooSmallForCapitalStock.values().stream().anyMatch(Boolean::booleanValue);

        int severity;
        if (hasAgeFlag && hasProcFlag && hasCapitalStockFlag) {
            severity = 9;
        } else if (hasCapitalStockFlag) {
            int otherFlagsCount = (hasAgeFlag ? 1 : 0) + procCount;
            if (otherFlagsCount == 0) {
                severity = 6;
            } else if (otherFlagsCount == 1) {
                severity = 7;
            } else {
                severity = 8;
            }
        } else if (hasAgeFlag && hasProcFlag) {
            if (procCount == 1) {
                severity = 5;
            } else {
                severity = 6;
            }
        } else if (hasProcFlag) {
            severity = 4;
        } else if (hasAgeFlag) {
            severity = 3;
        } else {
            severity = 3;
        }

        List<String> details = new ArrayList<>();
        if (isTooYoung) {
            details.add("Very young age (24 years or younger)");
        } else if (isTooOld) {
            details.add("Advanced age (60 years or older)");
        }

        if (hasEnforcementProceedings) {
            details.add("Has judicial enforcement proceedings");
        }
        if (hasBankruptcies) {
            details.add("Has bankruptcy history");
        }
        if (hasDebtProtests) {
            details.add("Has debt protests");
        }

        if (hasCapitalStockFlag) {
            long businessCount = tooSmallForCapitalStock.entrySet().stream()
                    .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
                    .count();
            details.add("Estimated total assets are incompatible with capital stock for " + businessCount + " business(es)");
        }

        String description = "Possible front man identified due to the following factors: " + String.join("; ", details) + ".";

        List<UUID> actorIds = new ArrayList<>();
        actorIds.add(personActorId);
        if (tooSmallForCapitalStock != null) {
            tooSmallForCapitalStock.forEach((businessActorId, isTooSmall) -> {
                if (Boolean.TRUE.equals(isTooSmall) && !actorIds.contains(businessActorId)) {
                    actorIds.add(businessActorId);
                }
            });
        }

        CreateRedFlagInput input = new CreateRedFlagInput(
                actorIds,
                publicProcurementId,
                null,
                null,
                "Possible Front Man Identification",
                severity,
                description
        );

        return redFlagUseCases.createRedFlag(input);
    }

    private boolean hasThisAtActorIndicators(List<String> stringList, List<ActorIndicator> actorIndicators){
        if (actorIndicators == null) return false;
        for (ActorIndicator actorIndicator : actorIndicators){
            if (actorIndicator == null || actorIndicator.getIndicatorType() == null) continue;
            for (String string : stringList){
                if(actorIndicator.getIndicatorType().equalsIgnoreCase(string)) return true;
            }
        }
        return false;
    }

    private Map<UUID, PersonCheckFrontManData> getPersonCheckFrontManMap(PublicProcurementInvestigationContext context) {
        Set<UUID> uuidSet = fillUUIDSet(context.getQsaList());
        Map<UUID, Person> peopleByUUID = fillPersonByUUIDMap(uuidSet);
        Map<UUID, PersonCheckFrontManData> personCheckFrontManDataMap = new HashMap<>();
        uuidSet.forEach(uuid-> {
            Person person = peopleByUUID.get(uuid);
            if (person == null) return;
            List<Asset> assets = context.getPersonAssets().get(uuid);
            BigDecimal totalEstimatedValue = getTotalEstimatedValue(assets);
            Integer age = person.getAge();
            List<ActorIndicator> actorIndicators = context.getPersonActorIndicators().get(uuid);
            Map<UUID, BigDecimal> stockCapitalByBusiness = getStockCapitalByBusinessPerAssociate(person, context);
            personCheckFrontManDataMap.put(uuid, new PersonCheckFrontManData(uuid, assets, actorIndicators, totalEstimatedValue, age, stockCapitalByBusiness));
        });

        return personCheckFrontManDataMap;
    }

    private Map<UUID, BigDecimal> getStockCapitalByBusinessPerAssociate(Person person, PublicProcurementInvestigationContext context){
        if (person == null || context.getQsaList() == null) return Map.of();

        Map<UUID, Integer> associatesByBusinessUUID = context.getQsaList().stream()
                .filter(qsa -> qsa != null && qsa.personList() != null && qsa.personList().contains(person))
                .collect(Collectors.toMap(
                        QSA::businessActorId,
                        qsa -> qsa.personList().size(),
                        (a, b) -> a
                ));

        if (context.getAllBusinessList() == null) return Map.of();

        return context.getAllBusinessList().stream()
                .filter(business -> business != null && business.getActor() != null && business.getCapitalStock() != null
                        && associatesByBusinessUUID.containsKey(business.getActor().getId())
                        && associatesByBusinessUUID.get(business.getActor().getId()) > 0
                )
                .collect(Collectors.toMap(
                        business -> business.getActor().getId(),
                        business -> business.getCapitalStock().divide(
                                BigDecimal.valueOf(associatesByBusinessUUID.get(business.getActor().getId())),
                                2,
                                RoundingMode.HALF_UP),
                        (a, b) -> a
                ));
    }

    private Map<UUID, Person> fillPersonByUUIDMap(Set<UUID> uuids){
        if (uuids == null || uuids.isEmpty()) return Map.of();
        List<Person> people = personUseCases.findByActorIds(uuids.stream().toList());
        if (people == null) return Map.of();
        return people.stream()
                .filter(person -> person != null && person.getActor() != null && person.getActor().getId() != null)
                .collect(Collectors.toMap(
                        person -> person.getActor().getId(),
                        person -> person,
                        (a, b) -> a
                ));
    }

    private Set<UUID> fillUUIDSet(List<QSA> qsaList) {
        Set<UUID> uuidSet = new HashSet<>();
        if (qsaList == null) return uuidSet;
        qsaList.forEach(qsa -> {
            if (qsa != null && qsa.personList() != null) {
                for(Person person : qsa.personList()){
                    if (person != null && person.getActor() != null && person.getActor().getId() != null) {
                        uuidSet.add(person.getActor().getId());
                    }
                }
            }
        });
        return uuidSet;
    }

    private BigDecimal getTotalEstimatedValue(List<Asset> assets) {
        if (assets != null && !assets.isEmpty()){
            List<BigDecimal> valueList = new ArrayList<>();

            for (Asset asset: assets){
                if (asset == null || asset.getEstimatedValue() == null) continue;
                BigDecimal assetValue = asset.getEstimatedValue();
                if(assetValue.compareTo(BigDecimal.ZERO) >= 0) valueList.add(assetValue);
            }

            return valueList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        }
        return BigDecimal.ZERO;
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
    record PersonCheckFrontManData(UUID uuid, List<Asset> assets, List<ActorIndicator> indicators, BigDecimal totalEstimatedAssetsValue, Integer age, Map<UUID, BigDecimal> stockCapitalByBusinessForAssociate){}

}
