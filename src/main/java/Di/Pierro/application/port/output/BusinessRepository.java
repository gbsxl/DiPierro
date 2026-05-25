package Di.Pierro.application.port.output;

import Di.Pierro.domain.model.Business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRepository {
    void save(Business business, UUID actorId);
    Optional<Business> findById(UUID id);
    List<Business> findAll();
}
