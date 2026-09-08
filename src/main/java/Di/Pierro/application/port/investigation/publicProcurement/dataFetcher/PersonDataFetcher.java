package Di.Pierro.application.port.investigation.publicProcurement.dataFetcher;

import Di.Pierro.application.dto.association.AssociationFilter;
import Di.Pierro.application.port.input.AssociationUseCases;
import Di.Pierro.application.port.input.BusinessUseCases;
import Di.Pierro.application.port.input.PersonUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.*;
import Di.Pierro.domain.enums.AssociationType;
import Di.Pierro.domain.model.*;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PersonDataFetcher {
    private final AssociationUseCases associationUseCases;
    private final PersonUseCases personUseCases;
    private final BusinessUseCases businessUseCases;

    public PersonDataFetcher(AssociationUseCases associationUseCases, PersonUseCases personUseCases, BusinessUseCases businessUseCases) {
        this.associationUseCases = associationUseCases;
        this.personUseCases = personUseCases;
        this.businessUseCases = businessUseCases;
    }

    public void fetchPersonData(PublicProcurementInvestigationContext context) {
        UUID publicProcurementActorId = context.getProcurement().getActor().getId();

        List<Person> classifiedBusinessPersonByQsa = getQsaPersonListByBusinessList(context.getAllBusinessList());
        List<Person> classifiedGovernmentSeed = fetchGovernmentSeedPersons(publicProcurementActorId);

        context.addGovernmentPersons(classifiedGovernmentSeed, 0);
        context.addBusinessPersons(classifiedBusinessPersonByQsa, 0);

        setPersonGraphsSeed(context);

        fetchHopOne(context);
        fetchHopTwo(context);
        fetchHopThree(context);
        setPersonGraphIntersection(context);
        setAllBusinessQSA(context);
    }

    public void setPersonGraphIntersection(PublicProcurementInvestigationContext context) {
        PersonGraphIntersection intersection = context.getPersonGraphIntersection();

        processGraphIntoIntersection(context.getGovernmentPersonGraph(), intersection);
        processGraphIntoIntersection(context.getBusinessPersonGraph(), intersection);
    }

    private void processGraphIntoIntersection(PersonGraph personGraph, PersonGraphIntersection intersection) {
        if (personGraph == null || personGraph.getPersonGraphItemList() == null) {
            return;
        }

        for (PersonGraphItem item : personGraph.getPersonGraphItemList()) {
            UUID currentId = item.actorId();

            intersection.addNode(currentId);

            LinkedList<ActorConnectionNode> connections = item.actorIdConnectionsOrder();

            if (connections != null && connections.size() >= 2) {
                UUID antecessorId = connections.get(connections.size() - 2).actorId();

                if (!antecessorId.equals(currentId)) {
                    intersection.addConnection(antecessorId, currentId);
                }
            }
        }
    }

    public void fetchHop(PublicProcurementInvestigationContext context, int targetHop) {
        List<Person> businessPersons = processHopForGraph(context.getBusinessPersonGraph(), targetHop, context.getAllBusinessList());
        List<Person> governmentPersons = processHopForGraph(context.getGovernmentPersonGraph(), targetHop, context.getAllBusinessList());

        context.addBusinessPersons(businessPersons, targetHop);
        context.addGovernmentPersons(governmentPersons, targetHop);
    }

    public void fetchHopOne(PublicProcurementInvestigationContext context) {
        fetchHop(context, 1);
    }

    public void fetchHopTwo(PublicProcurementInvestigationContext context) {
        fetchHop(context, 2);
    }

    public void fetchHopThree(PublicProcurementInvestigationContext context) {
        fetchHop(context, 3);
    }

    private List<Person> processHopForGraph(PersonGraph graph, int targetHop, List<Business> businessList) {
        int sourceHop = targetHop - 1;

        Map<UUID, PersonGraphItem> sourceHopMap = graph.getPersonGraphItemList().stream()
                .filter(item -> item.hop() == sourceHop)
                .collect(Collectors.toMap(PersonGraphItem::actorId, item -> item, (existing, replacement) -> existing));

        if (sourceHopMap.isEmpty()) {
            return Collections.emptyList();
        }

        Set<UUID> existingActorIdsInGraph = graph.getPersonGraphItemList().stream()
                .map(PersonGraphItem::actorId)
                .collect(Collectors.toSet());

        List<UUID> sourceActorIds = new ArrayList<>(sourceHopMap.keySet());

        AssociationFilter filterSource = new AssociationFilter(
                null, null, null, null, null,
                sourceActorIds,
                null, null, null, null, null, null, null, null, null, null, null
        );
        List<Association> associationsSource = associationUseCases.findByFilter(filterSource);

        List<RawConnection> rawConnections = new ArrayList<>();
        Set<UUID> targetActorIds = new HashSet<>();

        for (Association assoc : associationsSource) {
            UUID firstId = assoc.getFirstActor().getId();
            UUID secondId = assoc.getSecondActor().getId();

            PersonGraphItem parentItem = sourceHopMap.containsKey(firstId) ? sourceHopMap.get(firstId) : sourceHopMap.get(secondId);
            UUID targetId = sourceHopMap.containsKey(firstId) ? secondId : firstId;

            if (!existingActorIdsInGraph.contains(targetId)) {
                rawConnections.add(new RawConnection(parentItem, targetId));
                targetActorIds.add(targetId);
            }
        }

        if (targetActorIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Person> foundPersonsList = personUseCases.findAllByIds(new ArrayList<>(targetActorIds));
        Set<UUID> foundPersonIds = foundPersonsList.stream()
                .map(p -> p.getActor().getId()).collect(Collectors.toSet());

        List<Business> foundBusiness = businessUseCases.findAllByIds(new ArrayList<>(targetActorIds));
        businessList.addAll(foundBusiness);

        Set<UUID> foundBusinessIds = foundBusiness
                .stream().map(b -> b.getActor().getId()).collect(Collectors.toSet());

        Set<UUID> newlyAddedPersonIds = new HashSet<>();

        for (RawConnection rawConnection : rawConnections) {
            if (foundPersonIds.contains(rawConnection.targetId())) {
                PersonGraphItem parentItem = rawConnection.parentItem();
                UUID personId = rawConnection.targetId();

                LinkedList<ActorConnectionNode> order = new LinkedList<>(parentItem.actorIdConnectionsOrder());
                order.add(new ActorConnectionNode(personId, ActorType.PERSON));

                PersonGraphItem newItem = new PersonGraphItem(
                        generateUniqueId(),
                        personId,
                        targetHop,
                        false,
                        order
                );

                graph.addItem(newItem);
                graph.addConnection(new ConnectionOrder(newItem.id(), parentItem.id()));
                newlyAddedPersonIds.add(personId);
            }
        }

        List<RawConnection> businessConnections = rawConnections.stream()
                .filter(rawConnection -> foundBusinessIds.contains(rawConnection.targetId()))
                .toList();

        if (!businessConnections.isEmpty()) {
            List<UUID> businessIds = businessConnections.stream().map(RawConnection::targetId).distinct().toList();

            Map<UUID, List<PersonGraphItem>> parentsByBusinessId = new HashMap<>();

            for (RawConnection rawConnection : businessConnections) {
                parentsByBusinessId.computeIfAbsent(rawConnection.targetId(), k -> new ArrayList<>()).add(rawConnection.parentItem());
            }

            AssociationFilter filterBusiness = new AssociationFilter(
                    null, null, null, null, null,
                    businessIds,
                    null, null, null, null, null, null, null, null, null, null, null
            );
            List<Association> businessAssociations = associationUseCases.findByFilter(filterBusiness);

            Set<UUID> candidatePersonIds = new HashSet<>();
            for (Association association : businessAssociations) {
                candidatePersonIds.add(association.getFirstActor().getId());
                candidatePersonIds.add(association.getSecondActor().getId());
            }

            businessIds.forEach(candidatePersonIds::remove);
            candidatePersonIds.removeAll(existingActorIdsInGraph);

            if (!candidatePersonIds.isEmpty()) {
                List<Person> businessPersonsList = personUseCases.findAllByIds(new ArrayList<>(candidatePersonIds));
                Set<UUID> confirmedPersonIds = businessPersonsList.stream()
                        .map(p -> p.getActor().getId()).collect(Collectors.toSet());

                for (Association association : businessAssociations) {
                    UUID firstId = association.getFirstActor().getId();
                    UUID secondId = association.getSecondActor().getId();

                    UUID businessUUID = businessIds.contains(firstId) ? firstId : (businessIds.contains(secondId) ? secondId : null);
                    UUID personUUID = businessIds.contains(firstId) ? secondId : firstId;

                    if (businessUUID != null && confirmedPersonIds.contains(personUUID)) {
                        List<PersonGraphItem> parentItems = parentsByBusinessId.get(businessUUID);

                        if (parentItems != null) {
                            for (PersonGraphItem parentItem : parentItems) {
                                LinkedList<ActorConnectionNode> order = new LinkedList<>(parentItem.actorIdConnectionsOrder());
                                order.add(new ActorConnectionNode(businessUUID, ActorType.BUSINESS));
                                order.add(new ActorConnectionNode(personUUID, ActorType.PERSON));

                                PersonGraphItem newItem = new PersonGraphItem(
                                        generateUniqueId(),
                                        personUUID,
                                        targetHop,
                                        true,
                                        order
                                );

                                graph.addItem(newItem);
                                graph.addConnection(new ConnectionOrder(newItem.id(), parentItem.id()));
                                newlyAddedPersonIds.add(personUUID);
                            }
                        }
                    }
                }
            }
        }

        return personUseCases.findAllByIds(new ArrayList<>(newlyAddedPersonIds));
    }

    private Long generateUniqueId() {
        return Math.abs(UUID.randomUUID().getMostSignificantBits());
    }

    private record RawConnection(PersonGraphItem parentItem, UUID targetId) {}

    private void setPersonGraphsSeed(PublicProcurementInvestigationContext context) {
        List<Person> businessPersonHopZero = context.getBusinessPersonHopZero();
        List<Person> governmentPersonHopZero = context.getGovernmentPersonHopZero();

        List<PersonGraphItem> businessPersonGraphItemList = businessPersonHopZero.stream()
                .map(person -> new PersonGraphItem(
                        generateUniqueId(),
                        person.getActor().getId(),
                        0,
                        false,
                        new LinkedList<>(List.of(new ActorConnectionNode(person.getActor().getId(), ActorType.PERSON)))
                ))
                .toList();
        List<PersonGraphItem> governmentPersonGraphItemList = governmentPersonHopZero.stream()
                .map(person -> new PersonGraphItem(
                        generateUniqueId(),
                        person.getActor().getId(),
                        0,
                        false,
                        new LinkedList<>(List.of(new ActorConnectionNode(person.getActor().getId(), ActorType.PERSON)))
                ))
                .toList();

        businessPersonGraphItemList.forEach(personGraphItem -> context.getBusinessPersonGraph().addItem(personGraphItem));
        governmentPersonGraphItemList.forEach(personGraphItem -> context.getGovernmentPersonGraph().addItem(personGraphItem));
    }

    private List<Person> fetchGovernmentSeedPersons(UUID publicProcurementActorId){
        List<AssociationType> associationTypeList = List.of(
                AssociationType.AUTOR_ETP,
                AssociationType.AUTOR_TR,
                AssociationType.PREGOEIRO,
                AssociationType.MEMBRO_EQUIPE_APOIO,
                AssociationType.HOMOLOGADOR,
                AssociationType.PROCURADOR_JURIDICO,
                AssociationType.AUTORIDADE_DESIGNANTE
        );

        List<UUID> uuidList = getUuids(publicProcurementActorId, associationTypeList);

        return personUseCases.findAllByIds(uuidList);
    }

    public List<Person> getQsaPersonListByBusinessList(List<Business> businessList) {
        if (businessList == null || businessList.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> businessActorIds = businessList.stream().map(b -> b.getActor().getId()).toList();
        List<AssociationType> qsaAssociationTypes = List.of(
                AssociationType.SOCIO,
                AssociationType.SOCIO_ADMINISTRADOR,
                AssociationType.DIRETOR,
                AssociationType.CONSELHEIRO
        );

        AssociationFilter associationFilter = new AssociationFilter(
                null, null, null, null, null,
                businessActorIds,
                null,
                qsaAssociationTypes,
                null, null, null, null, null, null, null, null, null
        );

        List<Association> associations = associationUseCases.findByFilter(associationFilter);

        Set<UUID> personActorIds = associations.stream()
                .map(assoc -> businessActorIds.contains(assoc.getFirstActor().getId())
                        ? assoc.getSecondActor().getId()
                        : assoc.getFirstActor().getId())
                .collect(Collectors.toSet());

        return personUseCases.findAllByIds(new ArrayList<>(personActorIds));
    }

    public void setAllBusinessQSA(PublicProcurementInvestigationContext context) {
        List<Business> businessList = context.getAllBusinessList();
        if (businessList == null || businessList.isEmpty()) {
            return;
        }

        List<UUID> businessActorIds = businessList.stream().map(b -> b.getActor().getId()).toList();
        List<AssociationType> qsaAssociationTypes = List.of(
                AssociationType.SOCIO,
                AssociationType.SOCIO_ADMINISTRADOR,
                AssociationType.DIRETOR,
                AssociationType.CONSELHEIRO
        );

        AssociationFilter associationFilter = new AssociationFilter(
                null, null, null, null, null,
                businessActorIds,
                null,
                qsaAssociationTypes,
                null, null, null, null, null, null, null, null, null
        );

        List<Association> associations = associationUseCases.findByFilter(associationFilter);
        Set<UUID> allPersonIds = associations.stream()
                .map(assoc -> businessActorIds.contains(assoc.getFirstActor().getId())
                        ? assoc.getSecondActor().getId()
                        : assoc.getFirstActor().getId())
                .collect(Collectors.toSet());

        Map<UUID, Person> personMap = personUseCases.findAllByIds(new ArrayList<>(allPersonIds)).stream()
                .collect(Collectors.toMap(p -> p.getActor().getId(), p -> p, (a, b) -> a));

        Map<UUID, List<Person>> qsaPersonsByBusinessActorId = new HashMap<>();
        for (Association assoc : associations) {
            UUID businessId = businessActorIds.contains(assoc.getFirstActor().getId())
                    ? assoc.getFirstActor().getId()
                    : assoc.getSecondActor().getId();
            UUID personId = businessActorIds.contains(assoc.getFirstActor().getId())
                    ? assoc.getSecondActor().getId()
                    : assoc.getFirstActor().getId();

            Person person = personMap.get(personId);
            if (person != null) {
                qsaPersonsByBusinessActorId.computeIfAbsent(businessId, k -> new ArrayList<>()).add(person);
            }
        }

        for (Business business : businessList) {
            List<Person> qsaPersons = qsaPersonsByBusinessActorId.getOrDefault(business.getActor().getId(), Collections.emptyList());
            context.getQsaList().add(new QSA(business.getId(), business.getActor().getId(), qsaPersons));
        }
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
}