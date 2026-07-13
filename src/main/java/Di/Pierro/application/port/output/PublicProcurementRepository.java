package Di.Pierro.application.port.output;

import Di.Pierro.domain.model.PublicProcurement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicProcurementRepository {
    void save(PublicProcurement publicProcurement, UUID actorId);
    Optional<PublicProcurement> findById(UUID id);
    List<PublicProcurement> findAll();
}
