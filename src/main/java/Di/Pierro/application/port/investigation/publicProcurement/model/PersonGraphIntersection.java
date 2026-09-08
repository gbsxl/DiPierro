package Di.Pierro.application.port.investigation.publicProcurement.model;

import java.util.*;

public class PersonGraphIntersection {
    private final Map<UUID, PersonNode> graph = new HashMap<>();
    private final Map<UUID, List<LinkedList<ActorConnectionNode>>> connectionMapByUUID = new HashMap<>();

    public void addNode(UUID actorId) {
        if (!graph.containsKey(actorId)) {
            graph.put(actorId, new PersonNode(actorId, new HashSet<>()));
        }
    }

    public void addConnection(UUID fromAntecessorId, UUID toCurrentId) {
        addNode(fromAntecessorId);
        addNode(toCurrentId);

        graph.get(fromAntecessorId).connections().add(new Connection(toCurrentId));
    }

    public Map<UUID, PersonNode> getGraph() {
        return graph;
    }

    public boolean hasUUID(UUID uuid) {
        return graph.containsKey(uuid);
    }

    public void addTrajectory(UUID intersectionActorId, LinkedList<ActorConnectionNode> trajectory) {
        connectionMapByUUID.computeIfAbsent(intersectionActorId, k -> new ArrayList<>()).add(trajectory);
    }

    public Map<UUID, List<LinkedList<ActorConnectionNode>>> getConnectionMapByUUID() {
        return connectionMapByUUID;
    }
}
