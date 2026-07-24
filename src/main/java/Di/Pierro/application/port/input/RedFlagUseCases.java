package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.dto.redflag.RedFlagFilter;
import Di.Pierro.domain.model.RedFlag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RedFlagUseCases {
    void createRedFlag(CreateRedFlagInput createRedFlagInput);
    List<RedFlag> findAll();
    Optional<RedFlag> findById(UUID id);
    List<RedFlag> findByActorId(UUID id);
    List<RedFlag> findByPublicProcurementId(UUID id);
    List<RedFlag> findByTransactionId(UUID id);
    List<RedFlag> findByAssociationId(UUID id);
    List<RedFlag> findByType(String string);
    List<RedFlag> findBySeverity(Integer severity);
    List<RedFlag> findByFilter(RedFlagFilter filter);
    RedFlag updateById(UUID id, CreateRedFlagInput redFlag);
    void deleteById(UUID id);
}
