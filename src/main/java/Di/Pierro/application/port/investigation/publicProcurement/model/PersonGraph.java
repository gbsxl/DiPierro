package Di.Pierro.application.port.investigation.publicProcurement.model;

import java.util.*;

public class PersonGraph {
    private final Set<ConnectionOrder> connectionOrderSet;
    private final List<PersonGraphItem> personGraphItemList;
    private final Set<Long> existingIds;
    private final Set<UUID> existingUUIDs;

    public PersonGraph(Set<ConnectionOrder> connectionOrderSet, List<PersonGraphItem> personGraphItemList, Set<Long> existingIds, Set<UUID> existingUUIDs) {
        this.connectionOrderSet = connectionOrderSet;
        this.personGraphItemList = personGraphItemList;
        this.existingIds = existingIds;
        this.existingUUIDs = existingUUIDs;
    }

    public PersonGraph() {
        this.connectionOrderSet = new HashSet<>();
        this.personGraphItemList = new ArrayList<>();
        this.existingIds = new HashSet<>();
        this.existingUUIDs = new HashSet<>();
    }

    public void addItem(PersonGraphItem personGraphItem) {
        personGraphItemList.add(personGraphItem);
        existingIds.add(personGraphItem.id());
        existingUUIDs.add(personGraphItem.actorId());
    }

    public void addConnection(ConnectionOrder connectionOrder) {
        boolean bothIdAreDifferent = !connectionOrder.currentItemId().equals(connectionOrder.previousItemId());

        boolean isCurrentItem = existingIds.contains(connectionOrder.currentItemId());
        boolean isPreviousItem = existingIds.contains(connectionOrder.previousItemId());

        if (bothIdAreDifferent && isCurrentItem && isPreviousItem) {
            connectionOrderSet.add(connectionOrder);
        }
    }

    public List<PersonGraphItem> hasUUIDInThePersonGraph(UUID uuid){
        return personGraphItemList.stream().filter(personGraphItem -> personGraphItem.actorId().equals(uuid)).toList();
    }
    public boolean hasLongInThePersonGraph(Long id){
        return personGraphItemList.stream().anyMatch(personGraphItem -> personGraphItem.id().equals(id));
    }
    public boolean hasConnectionBetweenUUIDs(Long id1, Long id2){
        ConnectionOrder connectionOrder1 = new ConnectionOrder(id1, id2);
        ConnectionOrder connectionOrder2 = new ConnectionOrder(id2, id1);
        return connectionOrderSet.contains(connectionOrder1) || connectionOrderSet.contains(connectionOrder2);
    }

    public Set<ConnectionOrder> getConnectionOrderSet() {
        return connectionOrderSet;
    }

    public List<PersonGraphItem> getPersonGraphItemList() {
        return personGraphItemList;
    }
}