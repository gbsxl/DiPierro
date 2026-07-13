package Di.Pierro.application.port.output;

import Di.Pierro.domain.model.RedFlag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RedFlagRepository {
    void save(RedFlag redFlag, UUID actorId, UUID publicProcurementId);
    Optional<RedFlag> findById(UUID id);
    List<RedFlag> findAll();
}
