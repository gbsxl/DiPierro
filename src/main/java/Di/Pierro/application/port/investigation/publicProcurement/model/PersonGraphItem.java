package Di.Pierro.application.port.investigation.publicProcurement.model;

import java.util.LinkedList;
import java.util.UUID;

public record PersonGraphItem(
        Long id,
        UUID actorId,
        int hop,
        boolean hasIntermediary,
        LinkedList<ActorConnectionNode> actorIdConnectionsOrder
) {
}