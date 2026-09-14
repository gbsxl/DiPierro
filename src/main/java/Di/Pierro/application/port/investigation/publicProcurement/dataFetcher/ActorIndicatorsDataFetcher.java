package Di.Pierro.application.port.investigation.publicProcurement.dataFetcher;

import Di.Pierro.application.port.input.ActorIndicatorUseCases;
import Di.Pierro.application.port.investigation.publicProcurement.model.PublicProcurementInvestigationContext;
import Di.Pierro.domain.model.ActorIndicator;
import Di.Pierro.domain.model.Business;
import Di.Pierro.domain.model.Person;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ActorIndicatorsDataFetcher {
    private final ActorIndicatorUseCases actorIndicatorUseCases;

    public ActorIndicatorsDataFetcher(ActorIndicatorUseCases actorIndicatorUseCases) {
        this.actorIndicatorUseCases = actorIndicatorUseCases;
    }

    public void fetchActorIndicators(PublicProcurementInvestigationContext context){
        List<UUID> uuidList = fillUUIDSet(context.getAllPersonList(), context.getAllBusinessList());
        Map<UUID, List<ActorIndicator>> allActorIndicators = getAllActorIndicators(uuidList);
        context.getPersonActorIndicators().putAll(allActorIndicators);
    }

    private List<UUID> fillUUIDSet(List<Person> personList, List<Business> businessList){
        Set<UUID> uuidPersonSet = personList.stream()
                .map(person -> person.getActor().getId())
                .collect(Collectors.toSet());

        Set<UUID> uuidBusinessSet = businessList.stream()
                .map(business -> business.getActor().getId())
                .collect(Collectors.toSet());

        Set<UUID> uuidSet = new HashSet<>(uuidPersonSet);
        uuidSet.addAll(uuidBusinessSet);

        return uuidSet.stream().toList();
    }

    private Map<UUID, List<ActorIndicator>> getAllActorIndicators(List<UUID> uuidList){
        Map<UUID, List<ActorIndicator>> personActorIndicators = new HashMap<>();
        List<ActorIndicator> actorIndicators = actorIndicatorUseCases.findAllByActorsUUID(uuidList);

        for(ActorIndicator actorIndicator: actorIndicators){
            UUID uuid = actorIndicator.getActor().getId();
            personActorIndicators
                    .computeIfAbsent(uuid, k -> new ArrayList<>())
                    .add(actorIndicator);
        }
        return personActorIndicators;
    }
}
