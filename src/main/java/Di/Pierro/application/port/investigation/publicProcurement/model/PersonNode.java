package Di.Pierro.application.port.investigation.publicProcurement.model;

import java.util.Set;
import java.util.UUID;

public record PersonNode(
        UUID actorId,
        Set<Connection> connections
) {

}
