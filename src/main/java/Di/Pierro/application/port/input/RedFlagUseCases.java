package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.domain.model.RedFlag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RedFlagUseCases {
    void createRedFlag(CreateRedFlagInput createRedFlagInput);
    List<RedFlag> findAll();
    Optional<RedFlag> findById(UUID id);
}
