package Di.Pierro.application.port.investigation.publicProcurement.model;

import java.util.*;

public class TransactionGraph {
    Map<UUID, Set<TransactionConnection>> graph = new HashMap<>();
    Set<UUID> uuidSet = new HashSet<>();

    public Map<UUID, Set<TransactionConnection>> getGraph() {
        return graph;
    }

    public Set<UUID> getUuidSet() {
        return uuidSet;
    }

    public void addGraphItem(TransactionConnection transactionConnection){
        addUUIDs(transactionConnection);
        addConnections(transactionConnection);
    }

    private void addConnections(TransactionConnection transactionConnection){
        graph.get(transactionConnection.toEntityId()).add(transactionConnection);
        graph.get(transactionConnection.fromEntityId()).add(transactionConnection);
    }

    private void addUUIDs(TransactionConnection transactionConnection){
        if(!graph.containsKey(transactionConnection.toEntityId())) {
            graph.put(transactionConnection.toEntityId(), new HashSet<>());
            uuidSet.add(transactionConnection.toEntityId());
        }

        if(!graph.containsKey(transactionConnection.fromEntityId())) {
            graph.put(transactionConnection.fromEntityId(), new HashSet<>());
            uuidSet.add(transactionConnection.fromEntityId());
        }
    }
}