package Di.Pierro.application.port.investigation.publicProcurement.model;

import java.util.UUID;

public record ActorConnectionNode(
        UUID actorId,
        ActorType actorType
) {
}
