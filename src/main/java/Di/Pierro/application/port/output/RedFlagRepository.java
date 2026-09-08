package Di.Pierro.application.port.output;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.dto.redflag.RedFlagFilter;
import Di.Pierro.domain.model.RedFlag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RedFlagRepository {
    RedFlag save(RedFlag redFlag, List<UUID> actorIds, UUID publicProcurementId);
    Optional<RedFlag> findById(UUID id);
    List<RedFlag> findAll();
    List<RedFlag> findByActorId(UUID id);
    List<RedFlag> findByActorIds(List<UUID> actorIds);
    List<RedFlag> findByPublicProcurementId(UUID id);
    List<RedFlag> findByTransactionId(UUID id);
    List<RedFlag> findByAssociationId(UUID id);
    List<RedFlag> findByType(String string);
    List<RedFlag> findBySeverity(Integer severity);
    List<RedFlag> findByFilter(RedFlagFilter filter);
    RedFlag updateById(UUID id, CreateRedFlagInput redFlag);
    void deleteById(UUID id);
}
